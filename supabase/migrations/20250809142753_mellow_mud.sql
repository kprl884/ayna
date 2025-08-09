/*
  # Ayna App - Complete Database Schema

  1. New Tables
    - `profiles` - User profile information extending Supabase auth.users
    - `salons` - Salon/venue information with location data
    - `employees` - Salon staff members
    - `services` - Services offered by salons
    - `service_categories` - Service categorization
    - `appointments` - User bookings and scheduling
    - `time_slots` - Available appointment slots
    - `reviews` - User reviews and ratings
    - `waitlist_requests` - Waitlist when no slots available
    - `notifications` - User notifications
    - `payment_methods` - User payment information
    - `salon_images` - Salon photo gallery
    - `opening_hours` - Salon operating schedules
    - `buy_options` - Memberships and gift cards

  2. Security
    - Enable RLS on all tables
    - User-specific policies for data access
    - Admin policies for salon management
    - Public read access for salon discovery

  3. Performance
    - Indexes on frequently queried columns
    - Full-text search capabilities
    - Geographic queries for location-based features
*/

-- Enable required extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "postgis";

-- User profiles extending Supabase auth
CREATE TABLE IF NOT EXISTS profiles (
  id uuid PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
  name text NOT NULL,
  email text NOT NULL,
  phone_number text,
  profile_picture_url text,
  loyalty_points integer DEFAULT 0,
  member_since timestamptz DEFAULT now(),
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

-- Salons/Venues
CREATE TABLE IF NOT EXISTS salons (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  name text NOT NULL,
  description text,
  address text NOT NULL,
  city text NOT NULL,
  district text,
  latitude decimal(10, 8),
  longitude decimal(11, 8),
  phone_number text,
  rating decimal(3, 2) DEFAULT 0.0,
  review_count integer DEFAULT 0,
  venue_type text CHECK (venue_type IN ('EVERYONE', 'MALE_ONLY', 'FEMALE_ONLY')) DEFAULT 'EVERYONE',
  is_open boolean DEFAULT true,
  is_featured boolean DEFAULT false,
  tags text[] DEFAULT '{}',
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

-- Service categories
CREATE TABLE IF NOT EXISTS service_categories (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  name text NOT NULL UNIQUE,
  display_name text NOT NULL,
  emoji text,
  sort_order integer DEFAULT 0,
  created_at timestamptz DEFAULT now()
);

-- Services offered by salons
CREATE TABLE IF NOT EXISTS services (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  salon_id uuid NOT NULL REFERENCES salons(id) ON DELETE CASCADE,
  category_id uuid REFERENCES service_categories(id),
  name text NOT NULL,
  description text,
  price_cents integer NOT NULL DEFAULT 0,
  duration_minutes integer NOT NULL,
  gender_restriction text CHECK (gender_restriction IN ('MALE_ONLY', 'FEMALE_ONLY')),
  is_active boolean DEFAULT true,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

-- Salon employees/staff
CREATE TABLE IF NOT EXISTS employees (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  salon_id uuid NOT NULL REFERENCES salons(id) ON DELETE CASCADE,
  name text NOT NULL,
  role text NOT NULL,
  specialty text,
  image_url text,
  rating decimal(3, 2) DEFAULT 0.0,
  review_count integer DEFAULT 0,
  is_active boolean DEFAULT true,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

-- Opening hours for salons
CREATE TABLE IF NOT EXISTS opening_hours (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  salon_id uuid NOT NULL REFERENCES salons(id) ON DELETE CASCADE,
  day_of_week integer NOT NULL CHECK (day_of_week >= 0 AND day_of_week <= 6), -- 0 = Sunday
  is_open boolean DEFAULT true,
  open_time time,
  close_time time,
  created_at timestamptz DEFAULT now(),
  UNIQUE(salon_id, day_of_week)
);

-- Salon images
CREATE TABLE IF NOT EXISTS salon_images (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  salon_id uuid NOT NULL REFERENCES salons(id) ON DELETE CASCADE,
  image_url text NOT NULL,
  alt_text text,
  sort_order integer DEFAULT 0,
  created_at timestamptz DEFAULT now()
);

-- Available time slots
CREATE TABLE IF NOT EXISTS time_slots (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  salon_id uuid NOT NULL REFERENCES salons(id) ON DELETE CASCADE,
  employee_id uuid REFERENCES employees(id) ON DELETE SET NULL,
  service_id uuid REFERENCES services(id) ON DELETE CASCADE,
  start_time timestamptz NOT NULL,
  end_time timestamptz NOT NULL,
  is_available boolean DEFAULT true,
  created_at timestamptz DEFAULT now(),
  UNIQUE(salon_id, employee_id, start_time)
);

-- User appointments
CREATE TABLE IF NOT EXISTS appointments (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
  salon_id uuid NOT NULL REFERENCES salons(id) ON DELETE CASCADE,
  service_id uuid NOT NULL REFERENCES services(id) ON DELETE CASCADE,
  employee_id uuid REFERENCES employees(id) ON DELETE SET NULL,
  time_slot_id uuid REFERENCES time_slots(id) ON DELETE SET NULL,
  appointment_date timestamptz NOT NULL,
  duration_minutes integer NOT NULL,
  price_cents integer NOT NULL,
  status text CHECK (status IN ('UPCOMING', 'COMPLETED', 'CANCELLED')) DEFAULT 'UPCOMING',
  notes text,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

-- User reviews
CREATE TABLE IF NOT EXISTS reviews (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
  salon_id uuid NOT NULL REFERENCES salons(id) ON DELETE CASCADE,
  appointment_id uuid REFERENCES appointments(id) ON DELETE SET NULL,
  rating integer NOT NULL CHECK (rating >= 1 AND rating <= 5),
  comment text,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now(),
  UNIQUE(user_id, salon_id, appointment_id)
);

-- Waitlist requests
CREATE TABLE IF NOT EXISTS waitlist_requests (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
  salon_id uuid NOT NULL REFERENCES salons(id) ON DELETE CASCADE,
  service_id uuid NOT NULL REFERENCES services(id) ON DELETE CASCADE,
  preferred_date date NOT NULL,
  preferred_time_range text NOT NULL,
  status text CHECK (status IN ('PENDING', 'NOTIFIED', 'CONVERTED', 'EXPIRED')) DEFAULT 'PENDING',
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

-- User notifications
CREATE TABLE IF NOT EXISTS notifications (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
  title text NOT NULL,
  message text NOT NULL,
  type text NOT NULL CHECK (type IN ('APPOINTMENT_REMINDER', 'APPOINTMENT_CANCELLED', 'PROFILE_COMPLETION', 'PASSWORD_CHANGED')),
  is_read boolean DEFAULT false,
  action_text text,
  action_route text,
  created_at timestamptz DEFAULT now()
);

-- User payment methods
CREATE TABLE IF NOT EXISTS payment_methods (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
  type text NOT NULL CHECK (type IN ('VISA', 'MASTERCARD', 'PAYPAL', 'APPLE_PAY', 'GOOGLE_PAY')),
  last_four_digits text NOT NULL,
  expiry_date text,
  is_default boolean DEFAULT false,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

-- Buy options (memberships, gift cards)
CREATE TABLE IF NOT EXISTS buy_options (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  salon_id uuid NOT NULL REFERENCES salons(id) ON DELETE CASCADE,
  title text NOT NULL,
  description text,
  type text CHECK (type IN ('MEMBERSHIP', 'GIFT_CARD')) NOT NULL,
  price_cents integer,
  is_active boolean DEFAULT true,
  created_at timestamptz DEFAULT now()
);

-- User favorites/bookmarks
CREATE TABLE IF NOT EXISTS user_favorites (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
  salon_id uuid NOT NULL REFERENCES salons(id) ON DELETE CASCADE,
  created_at timestamptz DEFAULT now(),
  UNIQUE(user_id, salon_id)
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_salons_location ON salons USING GIST(ST_Point(longitude, latitude));
CREATE INDEX IF NOT EXISTS idx_salons_city ON salons(city);
CREATE INDEX IF NOT EXISTS idx_salons_rating ON salons(rating DESC);
CREATE INDEX IF NOT EXISTS idx_services_salon_id ON services(salon_id);
CREATE INDEX IF NOT EXISTS idx_services_price ON services(price_cents);
CREATE INDEX IF NOT EXISTS idx_appointments_user_id ON appointments(user_id);
CREATE INDEX IF NOT EXISTS idx_appointments_salon_id ON appointments(salon_id);
CREATE INDEX IF NOT EXISTS idx_appointments_date ON appointments(appointment_date);
CREATE INDEX IF NOT EXISTS idx_time_slots_salon_date ON time_slots(salon_id, start_time);
CREATE INDEX IF NOT EXISTS idx_reviews_salon_id ON reviews(salon_id);
CREATE INDEX IF NOT EXISTS idx_notifications_user_unread ON notifications(user_id, is_read);

-- Full-text search indexes
CREATE INDEX IF NOT EXISTS idx_salons_search ON salons USING gin(to_tsvector('english', name || ' ' || description));
CREATE INDEX IF NOT EXISTS idx_services_search ON services USING gin(to_tsvector('english', name || ' ' || description));

-- Enable Row Level Security
ALTER TABLE profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE salons ENABLE ROW LEVEL SECURITY;
ALTER TABLE services ENABLE ROW LEVEL SECURITY;
ALTER TABLE employees ENABLE ROW LEVEL SECURITY;
ALTER TABLE opening_hours ENABLE ROW LEVEL SECURITY;
ALTER TABLE salon_images ENABLE ROW LEVEL SECURITY;
ALTER TABLE time_slots ENABLE ROW LEVEL SECURITY;
ALTER TABLE appointments ENABLE ROW LEVEL SECURITY;
ALTER TABLE reviews ENABLE ROW LEVEL SECURITY;
ALTER TABLE waitlist_requests ENABLE ROW LEVEL SECURITY;
ALTER TABLE notifications ENABLE ROW LEVEL SECURITY;
ALTER TABLE payment_methods ENABLE ROW LEVEL SECURITY;
ALTER TABLE buy_options ENABLE ROW LEVEL SECURITY;
ALTER TABLE user_favorites ENABLE ROW LEVEL SECURITY;
ALTER TABLE service_categories ENABLE ROW LEVEL SECURITY;

-- RLS Policies

-- Profiles: Users can read/update their own profile
CREATE POLICY "Users can read own profile"
  ON profiles FOR SELECT
  TO authenticated
  USING (auth.uid() = id);

CREATE POLICY "Users can update own profile"
  ON profiles FOR UPDATE
  TO authenticated
  USING (auth.uid() = id);

CREATE POLICY "Users can insert own profile"
  ON profiles FOR INSERT
  TO authenticated
  WITH CHECK (auth.uid() = id);

-- Salons: Public read access, admin write access
CREATE POLICY "Anyone can read salons"
  ON salons FOR SELECT
  TO authenticated, anon
  USING (true);

CREATE POLICY "Salon owners can update their salons"
  ON salons FOR UPDATE
  TO authenticated
  USING (true); -- TODO: Add salon ownership logic

-- Services: Public read access
CREATE POLICY "Anyone can read services"
  ON services FOR SELECT
  TO authenticated, anon
  USING (true);

-- Employees: Public read access
CREATE POLICY "Anyone can read employees"
  ON employees FOR SELECT
  TO authenticated, anon
  USING (true);

-- Opening hours: Public read access
CREATE POLICY "Anyone can read opening hours"
  ON opening_hours FOR SELECT
  TO authenticated, anon
  USING (true);

-- Salon images: Public read access
CREATE POLICY "Anyone can read salon images"
  ON salon_images FOR SELECT
  TO authenticated, anon
  USING (true);

-- Time slots: Public read access
CREATE POLICY "Anyone can read time slots"
  ON time_slots FOR SELECT
  TO authenticated, anon
  USING (true);

-- Appointments: Users can manage their own appointments
CREATE POLICY "Users can read own appointments"
  ON appointments FOR SELECT
  TO authenticated
  USING (auth.uid() = user_id);

CREATE POLICY "Users can create own appointments"
  ON appointments FOR INSERT
  TO authenticated
  WITH CHECK (auth.uid() = user_id);

CREATE POLICY "Users can update own appointments"
  ON appointments FOR UPDATE
  TO authenticated
  USING (auth.uid() = user_id);

-- Reviews: Users can read all, manage their own
CREATE POLICY "Anyone can read reviews"
  ON reviews FOR SELECT
  TO authenticated, anon
  USING (true);

CREATE POLICY "Users can create own reviews"
  ON reviews FOR INSERT
  TO authenticated
  WITH CHECK (auth.uid() = user_id);

CREATE POLICY "Users can update own reviews"
  ON reviews FOR UPDATE
  TO authenticated
  USING (auth.uid() = user_id);

-- Waitlist: Users can manage their own requests
CREATE POLICY "Users can read own waitlist requests"
  ON waitlist_requests FOR SELECT
  TO authenticated
  USING (auth.uid() = user_id);

CREATE POLICY "Users can create own waitlist requests"
  ON waitlist_requests FOR INSERT
  TO authenticated
  WITH CHECK (auth.uid() = user_id);

-- Notifications: Users can read their own
CREATE POLICY "Users can read own notifications"
  ON notifications FOR SELECT
  TO authenticated
  USING (auth.uid() = user_id);

CREATE POLICY "Users can update own notifications"
  ON notifications FOR UPDATE
  TO authenticated
  USING (auth.uid() = user_id);

-- Payment methods: Users can manage their own
CREATE POLICY "Users can read own payment methods"
  ON payment_methods FOR SELECT
  TO authenticated
  USING (auth.uid() = user_id);

CREATE POLICY "Users can manage own payment methods"
  ON payment_methods FOR ALL
  TO authenticated
  USING (auth.uid() = user_id)
  WITH CHECK (auth.uid() = user_id);

-- Buy options: Public read access
CREATE POLICY "Anyone can read buy options"
  ON buy_options FOR SELECT
  TO authenticated, anon
  USING (true);

-- User favorites: Users can manage their own
CREATE POLICY "Users can manage own favorites"
  ON user_favorites FOR ALL
  TO authenticated
  USING (auth.uid() = user_id)
  WITH CHECK (auth.uid() = user_id);

-- Service categories: Public read access
CREATE POLICY "Anyone can read service categories"
  ON service_categories FOR SELECT
  TO authenticated, anon
  USING (true);

-- Functions for updating ratings
CREATE OR REPLACE FUNCTION update_salon_rating()
RETURNS TRIGGER AS $$
BEGIN
  UPDATE salons 
  SET 
    rating = (
      SELECT COALESCE(AVG(rating), 0)
      FROM reviews 
      WHERE salon_id = COALESCE(NEW.salon_id, OLD.salon_id)
    ),
    review_count = (
      SELECT COUNT(*)
      FROM reviews 
      WHERE salon_id = COALESCE(NEW.salon_id, OLD.salon_id)
    ),
    updated_at = now()
  WHERE id = COALESCE(NEW.salon_id, OLD.salon_id);
  
  RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

-- Trigger to update salon ratings when reviews change
CREATE TRIGGER update_salon_rating_trigger
  AFTER INSERT OR UPDATE OR DELETE ON reviews
  FOR EACH ROW
  EXECUTE FUNCTION update_salon_rating();

-- Function to update employee ratings
CREATE OR REPLACE FUNCTION update_employee_rating()
RETURNS TRIGGER AS $$
BEGIN
  UPDATE employees 
  SET 
    rating = (
      SELECT COALESCE(AVG(r.rating), 0)
      FROM reviews r
      JOIN appointments a ON r.appointment_id = a.id
      WHERE a.employee_id = COALESCE(NEW.employee_id, OLD.employee_id)
    ),
    review_count = (
      SELECT COUNT(*)
      FROM reviews r
      JOIN appointments a ON r.appointment_id = a.id
      WHERE a.employee_id = COALESCE(NEW.employee_id, OLD.employee_id)
    )
  WHERE id = COALESCE(NEW.employee_id, OLD.employee_id);
  
  RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

-- Function to handle profile creation
CREATE OR REPLACE FUNCTION handle_new_user()
RETURNS TRIGGER AS $$
BEGIN
  INSERT INTO profiles (id, name, email)
  VALUES (
    NEW.id,
    COALESCE(NEW.raw_user_meta_data->>'name', 'User'),
    NEW.email
  );
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to create profile when user signs up
CREATE TRIGGER on_auth_user_created
  AFTER INSERT ON auth.users
  FOR EACH ROW
  EXECUTE FUNCTION handle_new_user();

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = now();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Triggers for updated_at
CREATE TRIGGER update_profiles_updated_at
  BEFORE UPDATE ON profiles
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_salons_updated_at
  BEFORE UPDATE ON salons
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_services_updated_at
  BEFORE UPDATE ON services
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_employees_updated_at
  BEFORE UPDATE ON employees
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_appointments_updated_at
  BEFORE UPDATE ON appointments
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_reviews_updated_at
  BEFORE UPDATE ON reviews
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_waitlist_requests_updated_at
  BEFORE UPDATE ON waitlist_requests
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_payment_methods_updated_at
  BEFORE UPDATE ON payment_methods
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();