/*
  # Seed Initial Data for Ayna App

  1. Service Categories
    - Hair & Styling, Nails, Massage, Skincare, etc.

  2. Sample Salons
    - Istanbul-based salons with realistic data

  3. Sample Services
    - Various beauty and grooming services

  4. Sample Employees
    - Staff members with specialties
*/

-- Insert service categories
INSERT INTO service_categories (name, display_name, emoji, sort_order) VALUES
('FEATURED', 'Featured', '⭐', 1),
('CONSULTATION', 'Consultation', '💬', 2),
('MENS_CUT', 'Men''s Cut', '✂️', 3),
('WOMENS_HAIRCUT', 'Women''s Haircut', '💇‍♀️', 4),
('STYLE', 'Style', '💄', 5),
('COLOR_APPLICATION', 'Color Application', '🎨', 6),
('QIQI_STRAIGHTENING', 'QIQI Straightening', '📏', 7),
('KIDS', 'Kids', '👶', 8),
('NAILS', 'Nails', '💅', 9),
('MASSAGE', 'Massage', '💆', 10),
('SKINCARE', 'Skincare', '🧴', 11),
('MAKEUP', 'Makeup', '💋', 12);

-- Insert sample salons
INSERT INTO salons (id, name, description, address, city, district, latitude, longitude, phone_number, venue_type, is_featured, tags) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Emre''s Barbershop Dolapdere', 'Traditional Turkish barbershop offering premium men''s grooming services in the heart of Dolapdere.', 'Dolapdere Mahallesi, 34384 Istanbul', 'Istanbul', 'Dolapdere', 41.0082, 28.9784, '+90 212 555 0101', 'MALE_ONLY', true, '{"Barbershop", "Traditional"}'),
('550e8400-e29b-41d4-a716-446655440002', 'Ayna Beauty Studio', 'Premium beauty studio specializing in nail care and beauty treatments.', 'Fenerbahçe Mahallesi, Rüştiye Sokak 9, Istanbul', 'Istanbul', 'Fenerbahçe', 40.9789, 29.0372, '+90 216 555 0202', 'FEMALE_ONLY', true, '{"Beauty Studio", "Nail Salon"}'),
('550e8400-e29b-41d4-a716-446655440003', 'Serenity Spa & Wellness', 'Luxury spa offering traditional and modern wellness treatments in Bebek.', 'Bebek Mahallesi, 34342 Istanbul', 'Istanbul', 'Bebek', 41.0766, 29.0434, '+90 212 555 0303', 'EVERYONE', false, '{"Spa", "Wellness"}'),
('550e8400-e29b-41d4-a716-446655440004', 'Elite Hair Studio', 'High-end hair studio in Nişantaşı offering premium hair services for women.', 'Nişantaşı Mahallesi, 34365 Istanbul', 'Istanbul', 'Nişantaşı', 41.0460, 28.9940, '+90 212 555 0404', 'EVERYONE', false, '{"Hair Salon", "Professional"}'),
('550e8400-e29b-41d4-a716-446655440005', 'Hair Etc. Studio', 'Hair Etc. Studio offers the most unique hair experience in Cyprus. We are a team of creators working with people on a daily basis.', '17D, Themistokli Dervi Str., "THE CITY HOUSE" Bld., Nicosia', 'Nicosia', 'City Center', 35.1856, 33.3823, '+357 22 555 0505', 'EVERYONE', true, '{"Hair Studio", "Creative"}');

-- Insert salon images
INSERT INTO salon_images (salon_id, image_url, sort_order) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'https://images.unsplash.com/photo-1585747860715-2ba37e788b70?w=800', 1),
('550e8400-e29b-41d4-a716-446655440001', 'https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?w=800', 2),
('550e8400-e29b-41d4-a716-446655440001', 'https://images.unsplash.com/photo-1503951914875-452162b0f3f1?w=800', 3),
('550e8400-e29b-41d4-a716-446655440002', 'https://images.unsplash.com/photo-1580618672591-eb180b1a973f?w=800', 1),
('550e8400-e29b-41d4-a716-446655440002', 'https://images.unsplash.com/photo-1522336284037-91f7da073525?w=800', 2),
('550e8400-e29b-41d4-a716-446655440003', 'https://images.unsplash.com/photo-1544161515-4ab6ce6db874?w=800', 1),
('550e8400-e29b-41d4-a716-446655440003', 'https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=800', 2),
('550e8400-e29b-41d4-a716-446655440004', 'https://images.unsplash.com/photo-1562322140-8baeececf3df?w=800', 1),
('550e8400-e29b-41d4-a716-446655440005', 'https://images.pexels.com/photos/3993449/pexels-photo-3993449.jpeg?w=800', 1),
('550e8400-e29b-41d4-a716-446655440005', 'https://images.pexels.com/photos/3993450/pexels-photo-3993450.jpeg?w=800', 2);

-- Insert opening hours
INSERT INTO opening_hours (salon_id, day_of_week, is_open, open_time, close_time) VALUES
-- Emre's Barbershop (closed Sunday)
('550e8400-e29b-41d4-a716-446655440001', 0, false, NULL, NULL), -- Sunday
('550e8400-e29b-41d4-a716-446655440001', 1, true, '09:00', '19:00'), -- Monday
('550e8400-e29b-41d4-a716-446655440001', 2, true, '09:00', '19:00'), -- Tuesday
('550e8400-e29b-41d4-a716-446655440001', 3, true, '09:00', '19:00'), -- Wednesday
('550e8400-e29b-41d4-a716-446655440001', 4, true, '09:00', '19:00'), -- Thursday
('550e8400-e29b-41d4-a716-446655440001', 5, true, '09:00', '20:00'), -- Friday
('550e8400-e29b-41d4-a716-446655440001', 6, true, '08:00', '20:00'), -- Saturday

-- Ayna Beauty Studio (open 7 days)
('550e8400-e29b-41d4-a716-446655440002', 0, true, '11:00', '19:00'), -- Sunday
('550e8400-e29b-41d4-a716-446655440002', 1, true, '10:00', '20:00'), -- Monday
('550e8400-e29b-41d4-a716-446655440002', 2, true, '10:00', '20:00'), -- Tuesday
('550e8400-e29b-41d4-a716-446655440002', 3, true, '10:00', '20:00'), -- Wednesday
('550e8400-e29b-41d4-a716-446655440002', 4, true, '10:00', '20:00'), -- Thursday
('550e8400-e29b-41d4-a716-446655440002', 5, true, '10:00', '21:00'), -- Friday
('550e8400-e29b-41d4-a716-446655440002', 6, true, '09:00', '21:00'), -- Saturday

-- Hair Etc. Studio (closed Monday)
('550e8400-e29b-41d4-a716-446655440005', 0, false, NULL, NULL), -- Sunday
('550e8400-e29b-41d4-a716-446655440005', 1, false, NULL, NULL), -- Monday
('550e8400-e29b-41d4-a716-446655440005', 2, true, '09:00', '19:00'), -- Tuesday
('550e8400-e29b-41d4-a716-446655440005', 3, true, '09:00', '19:00'), -- Wednesday
('550e8400-e29b-41d4-a716-446655440005', 4, true, '09:30', '17:30'), -- Thursday
('550e8400-e29b-41d4-a716-446655440005', 5, true, '09:00', '19:00'), -- Friday
('550e8400-e29b-41d4-a716-446655440005', 6, true, '08:30', '17:00'); -- Saturday

-- Insert employees
INSERT INTO employees (id, salon_id, name, role, specialty, image_url, rating, review_count) VALUES
('660e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', 'Emre Demir', 'Master Barber', 'Traditional cuts and shaves', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400', 4.9, 45),
('660e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440001', 'Ali Yılmaz', 'Hair Stylist', 'Modern styling', 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400', 4.8, 32),
('660e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440002', 'Ayla Kaya', 'Senior Nail Artist', 'Nail art and spa treatments', 'https://images.unsplash.com/photo-1494790108755-2616b612b3da?w=400', 4.9, 78),
('660e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440002', 'Zeynep Özkan', 'Beauty Specialist', 'Skincare and beauty', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400', 4.8, 56),
('660e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440003', 'Mehmet Güler', 'Massage Therapist', 'Thai and oil massage', 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=400', 4.9, 89),
('660e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440004', 'Canan Arslan', 'Senior Hair Stylist', 'Women''s cuts and color', 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=400', 4.8, 92),
('660e8400-e29b-41d4-a716-446655440007', '550e8400-e29b-41d4-a716-446655440005', 'Marios', 'Creative Director', 'Artistic hair design', NULL, 5.0, 156),
('660e8400-e29b-41d4-a716-446655440008', '550e8400-e29b-41d4-a716-446655440005', 'Ankit', 'Support Team', 'Customer service', NULL, 5.0, 89),
('660e8400-e29b-41d4-a716-446655440009', '550e8400-e29b-41d4-a716-446655440005', 'Fanouria', 'Stylist', 'Modern styling', NULL, 5.0, 134);

-- Insert services
INSERT INTO services (salon_id, category_id, name, description, price_cents, duration_minutes, gender_restriction) VALUES
-- Emre's Barbershop services
('550e8400-e29b-41d4-a716-446655440001', (SELECT id FROM service_categories WHERE name = 'MENS_CUT'), 'Haircut / Saç Kesimi', 'Professional men''s haircut with styling', 70000, 60, 'MALE_ONLY'),
('550e8400-e29b-41d4-a716-446655440001', (SELECT id FROM service_categories WHERE name = 'MENS_CUT'), 'Haircut & Shave / Saç Kesimi & Sakal Tıraşı', 'Complete grooming package', 117500, 90, 'MALE_ONLY'),
('550e8400-e29b-41d4-a716-446655440001', (SELECT id FROM service_categories WHERE name = 'FEATURED'), 'Full Service / Komple Bakım', 'Premium grooming experience', 188000, 120, 'MALE_ONLY'),

-- Ayna Beauty Studio services
('550e8400-e29b-41d4-a716-446655440002', (SELECT id FROM service_categories WHERE name = 'NAILS'), 'Spa Pedikür (Spa Pedicure)', 'Relaxing spa pedicure treatment', 95000, 55, 'FEMALE_ONLY'),
('550e8400-e29b-41d4-a716-446655440002', (SELECT id FROM service_categories WHERE name = 'NAILS'), 'SMART Manikür + Kalıcı Oje', 'Smart manicure with gel polish', 120000, 100, 'FEMALE_ONLY'),
('550e8400-e29b-41d4-a716-446655440002', (SELECT id FROM service_categories WHERE name = 'NAILS'), 'Nail Art', 'Creative nail art designs', 0, 20, 'FEMALE_ONLY'),

-- Serenity Spa services
('550e8400-e29b-41d4-a716-446655440003', (SELECT id FROM service_categories WHERE name = 'MASSAGE'), '60 Min Thai with Oil Massage', 'Traditional Thai massage with aromatic oils', 160000, 75, NULL),
('550e8400-e29b-41d4-a716-446655440003', (SELECT id FROM service_categories WHERE name = 'MASSAGE'), '30 Min Foot Massage', 'Relaxing foot massage therapy', 80000, 30, NULL),
('550e8400-e29b-41d4-a716-446655440003', (SELECT id FROM service_categories WHERE name = 'MASSAGE'), '60 Min Oil Massage', 'Full body oil massage', 160000, 75, NULL),

-- Elite Hair Studio services
('550e8400-e29b-41d4-a716-446655440004', (SELECT id FROM service_categories WHERE name = 'WOMENS_HAIRCUT'), 'Women''s Cut & Style', 'Professional women''s haircut and styling', 200000, 90, NULL),
('550e8400-e29b-41d4-a716-446655440004', (SELECT id FROM service_categories WHERE name = 'COLOR_APPLICATION'), 'Color Treatment', 'Hair coloring and highlights', 350000, 180, NULL),
('550e8400-e29b-41d4-a716-446655440004', (SELECT id FROM service_categories WHERE name = 'STYLE'), 'Blowout', 'Professional hair blowout', 100000, 45, NULL),

-- Hair Etc. Studio services (matching your mock data)
('550e8400-e29b-41d4-a716-446655440005', (SELECT id FROM service_categories WHERE name = 'FEATURED'), 'CUT', 'Professional haircut with styling', 2000, 65, 'MALE_ONLY'),
('550e8400-e29b-41d4-a716-446655440005', (SELECT id FROM service_categories WHERE name = 'FEATURED'), 'BLOW DRY | BRUSH STYLE', 'Professional blow dry and brush styling', 2000, 60, 'FEMALE_ONLY'),
('550e8400-e29b-41d4-a716-446655440005', (SELECT id FROM service_categories WHERE name = 'FEATURED'), 'HAIRCUT & FINISH', 'Complete haircut and finishing', 4000, 90, NULL),
('550e8400-e29b-41d4-a716-446655440005', (SELECT id FROM service_categories WHERE name = 'FEATURED'), 'COLOR | ROOTS & FINISH', 'Root color touch-up with finish', 4000, 105, 'FEMALE_ONLY'),
('550e8400-e29b-41d4-a716-446655440005', (SELECT id FROM service_categories WHERE name = 'CONSULTATION'), 'EXTENSION CONSULTATION', 'Hair extension consultation', 0, 10, NULL),
('550e8400-e29b-41d4-a716-446655440005', (SELECT id FROM service_categories WHERE name = 'QIQI_STRAIGHTENING'), 'QIQI | STRAIGHTENING TREATMENT - MAN', 'Professional straightening for men', 7000, 140, 'MALE_ONLY'),
('550e8400-e29b-41d4-a716-446655440005', (SELECT id FROM service_categories WHERE name = 'KIDS'), 'GIRLS | 1 – 12 Years', 'Haircut for girls aged 1-12', 2400, 70, 'FEMALE_ONLY'),
('550e8400-e29b-41d4-a716-446655440005', (SELECT id FROM service_categories WHERE name = 'KIDS'), 'BOYS | 1 – 12 Years', 'Haircut for boys aged 1-12', 1400, 45, 'MALE_ONLY');

-- Insert buy options
INSERT INTO buy_options (salon_id, title, description, type, price_cents) VALUES
('550e8400-e29b-41d4-a716-446655440005', 'Memberships', 'Bundle your services in to a membership', 'MEMBERSHIP', NULL),
('550e8400-e29b-41d4-a716-446655440005', 'Gift card', 'Treat yourself or a friend to future visits', 'GIFT_CARD', NULL),
('550e8400-e29b-41d4-a716-446655440001', 'VIP Membership', 'Premium barbershop membership with benefits', 'MEMBERSHIP', 50000),
('550e8400-e29b-41d4-a716-446655440002', 'Beauty Package', 'Monthly beauty treatment package', 'MEMBERSHIP', 75000);

-- Update salon ratings based on mock data
UPDATE salons SET rating = 5.0, review_count = 69 WHERE id = '550e8400-e29b-41d4-a716-446655440001';
UPDATE salons SET rating = 5.0, review_count = 158 WHERE id = '550e8400-e29b-41d4-a716-446655440002';
UPDATE salons SET rating = 4.8, review_count = 234 WHERE id = '550e8400-e29b-41d4-a716-446655440003';
UPDATE salons SET rating = 4.7, review_count = 89 WHERE id = '550e8400-e29b-41d4-a716-446655440004';
UPDATE salons SET rating = 5.0, review_count = 3645 WHERE id = '550e8400-e29b-41d4-a716-446655440005';