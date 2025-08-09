/*
  # Geographic Functions for Location-Based Queries

  1. Functions
    - salons_within_radius: Find salons within specified radius
    - calculate_distance: Calculate distance between two points

  2. Performance
    - Uses PostGIS for efficient geographic calculations
    - Proper indexing for fast queries
*/

-- Function to find salons within a radius
CREATE OR REPLACE FUNCTION salons_within_radius(
  lat decimal,
  lng decimal,
  radius_km integer DEFAULT 10
)
RETURNS TABLE (
  id uuid,
  name text,
  description text,
  address text,
  city text,
  district text,
  latitude decimal,
  longitude decimal,
  phone_number text,
  rating decimal,
  review_count integer,
  venue_type text,
  is_open boolean,
  is_featured boolean,
  tags text[],
  distance_km decimal,
  created_at timestamptz,
  updated_at timestamptz
) AS $$
BEGIN
  RETURN QUERY
  SELECT 
    s.id,
    s.name,
    s.description,
    s.address,
    s.city,
    s.district,
    s.latitude,
    s.longitude,
    s.phone_number,
    s.rating,
    s.review_count,
    s.venue_type,
    s.is_open,
    s.is_featured,
    s.tags,
    ROUND(
      ST_Distance(
        ST_Point(lng, lat)::geography,
        ST_Point(s.longitude, s.latitude)::geography
      ) / 1000, 2
    ) as distance_km,
    s.created_at,
    s.updated_at
  FROM salons s
  WHERE s.latitude IS NOT NULL 
    AND s.longitude IS NOT NULL
    AND ST_DWithin(
      ST_Point(lng, lat)::geography,
      ST_Point(s.longitude, s.latitude)::geography,
      radius_km * 1000
    )
  ORDER BY distance_km ASC;
END;
$$ LANGUAGE plpgsql;

-- Function to calculate distance between two points
CREATE OR REPLACE FUNCTION calculate_distance(
  lat1 decimal,
  lng1 decimal,
  lat2 decimal,
  lng2 decimal
)
RETURNS decimal AS $$
BEGIN
  RETURN ROUND(
    ST_Distance(
      ST_Point(lng1, lat1)::geography,
      ST_Point(lng2, lat2)::geography
    ) / 1000, 2
  );
END;
$$ LANGUAGE plpgsql;

-- Function to get available time slots for a date range
CREATE OR REPLACE FUNCTION get_available_slots(
  p_salon_id uuid,
  p_service_id uuid,
  p_start_date date,
  p_end_date date DEFAULT NULL
)
RETURNS TABLE (
  id uuid,
  salon_id uuid,
  employee_id uuid,
  service_id uuid,
  start_time timestamptz,
  end_time timestamptz,
  is_available boolean,
  created_at timestamptz
) AS $$
BEGIN
  IF p_end_date IS NULL THEN
    p_end_date := p_start_date;
  END IF;

  RETURN QUERY
  SELECT 
    ts.id,
    ts.salon_id,
    ts.employee_id,
    ts.service_id,
    ts.start_time,
    ts.end_time,
    ts.is_available,
    ts.created_at
  FROM time_slots ts
  WHERE ts.salon_id = p_salon_id
    AND ts.service_id = p_service_id
    AND ts.is_available = true
    AND DATE(ts.start_time) >= p_start_date
    AND DATE(ts.start_time) <= p_end_date
    AND ts.start_time > NOW() -- Only future slots
  ORDER BY ts.start_time ASC;
END;
$$ LANGUAGE plpgsql;

-- Function to generate time slots for a salon
CREATE OR REPLACE FUNCTION generate_time_slots(
  p_salon_id uuid,
  p_service_id uuid,
  p_employee_id uuid,
  p_date date,
  p_start_hour integer DEFAULT 9,
  p_end_hour integer DEFAULT 18,
  p_slot_duration_minutes integer DEFAULT 30
)
RETURNS integer AS $$
DECLARE
  slot_start timestamptz;
  slot_end timestamptz;
  current_time timestamptz;
  end_time timestamptz;
  slots_created integer := 0;
BEGIN
  -- Calculate start and end times for the day
  current_time := p_date + (p_start_hour || ' hours')::interval;
  end_time := p_date + (p_end_hour || ' hours')::interval;

  -- Generate slots
  WHILE current_time < end_time LOOP
    slot_start := current_time;
    slot_end := current_time + (p_slot_duration_minutes || ' minutes')::interval;

    -- Check if slot doesn't conflict with existing appointments
    IF NOT EXISTS (
      SELECT 1 FROM appointments a
      WHERE a.salon_id = p_salon_id
        AND a.employee_id = p_employee_id
        AND a.status = 'UPCOMING'
        AND (
          (a.appointment_date <= slot_start AND a.appointment_date + (a.duration_minutes || ' minutes')::interval > slot_start)
          OR
          (a.appointment_date < slot_end AND a.appointment_date + (a.duration_minutes || ' minutes')::interval >= slot_end)
          OR
          (a.appointment_date >= slot_start AND a.appointment_date + (a.duration_minutes || ' minutes')::interval <= slot_end)
        )
    ) THEN
      -- Insert the time slot
      INSERT INTO time_slots (salon_id, employee_id, service_id, start_time, end_time, is_available)
      VALUES (p_salon_id, p_employee_id, p_service_id, slot_start, slot_end, true);
      
      slots_created := slots_created + 1;
    END IF;

    current_time := current_time + (p_slot_duration_minutes || ' minutes')::interval;
  END LOOP;

  RETURN slots_created;
END;
$$ LANGUAGE plpgsql;

-- Function to update salon statistics
CREATE OR REPLACE FUNCTION update_salon_statistics()
RETURNS void AS $$
BEGIN
  -- Update all salon ratings and review counts
  UPDATE salons 
  SET 
    rating = COALESCE(salon_stats.avg_rating, 0),
    review_count = COALESCE(salon_stats.review_count, 0),
    updated_at = now()
  FROM (
    SELECT 
      r.salon_id,
      AVG(r.rating) as avg_rating,
      COUNT(r.id) as review_count
    FROM reviews r
    GROUP BY r.salon_id
  ) salon_stats
  WHERE salons.id = salon_stats.salon_id;
END;
$$ LANGUAGE plpgsql;

-- Function to clean up old time slots
CREATE OR REPLACE FUNCTION cleanup_old_time_slots()
RETURNS integer AS $$
DECLARE
  deleted_count integer;
BEGIN
  DELETE FROM time_slots 
  WHERE start_time < NOW() - interval '1 day'
    AND is_available = true;
  
  GET DIAGNOSTICS deleted_count = ROW_COUNT;
  RETURN deleted_count;
END;
$$ LANGUAGE plpgsql;

-- Function to expire old waitlist requests
CREATE OR REPLACE FUNCTION expire_old_waitlist_requests()
RETURNS integer AS $$
DECLARE
  expired_count integer;
BEGIN
  UPDATE waitlist_requests 
  SET status = 'EXPIRED'
  WHERE status = 'PENDING'
    AND preferred_date < CURRENT_DATE - interval '7 days';
  
  GET DIAGNOSTICS expired_count = ROW_COUNT;
  RETURN expired_count;
END;
$$ LANGUAGE plpgsql;