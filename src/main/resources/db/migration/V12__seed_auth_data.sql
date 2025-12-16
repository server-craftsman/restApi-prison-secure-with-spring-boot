-- V12: Seed Authentication Data
-- Sample users, prisons, zones, and tablets with VALID UUID v4

-- ============================================
-- PRISONS
-- ============================================
INSERT INTO prisons (id, prison_code, prison_name, location, address, capacity, current_population, status, established_date) VALUES
('a0000001-e29b-41d4-a716-446655440001', 'TCG-CC', 'Trại giam Củ Chi', 'Củ Chi, TP.HCM', '123 Đường Tỉnh lộ 8, Củ Chi, TP.HCM', 500, 0, 'ACTIVE', '2020-01-01'),
('a0000002-e29b-41d4-a716-446655440002', 'TCG-XL', 'Trại giam Xuân Lộc', 'Xuân Lộc, Đồng Nai', '456 Quốc lộ 1A, Xuân Lộc, Đồng Nai', 300, 0, 'ACTIVE', '2018-06-15'),
('a0000003-e29b-41d4-a716-446655440003', 'TCG-BT', 'Trại giam Bà Rịa', 'Bà Rịa, Bà Rịa - Vũng Tàu', '789 Lê Duẩn, Bà Rịa', 200, 0, 'ACTIVE', '2019-03-20');

-- ============================================
-- USERS - WARDENS (Quản đốc)
-- ============================================
-- Password: 123456 (BCrypt hashed)
INSERT INTO users (id, username, password_hash, email, full_name, user_type, status, phone_number) VALUES
('b0000001-e29b-41d4-a716-446655440001', 'warden_cuchi', '$2a$12$2StRNESZsxkshKPK3yJ6T.wtIfHmRUFojRzO8h6l87ZalaHlb.Oxu', 'warden.cuchi@prison.gov.vn', 'Nguyễn Văn Quản Đốc', 'WARDEN', 'ACTIVE', '0901234567'),
('b0000002-e29b-41d4-a716-446655440002', 'warden_xuanloc', '$2a$12$zHOdL542o4DIQ3/x7/rtw.lBKhj3uQM/WotJKIZoyC2qugI0kA85i', 'warden.xuanloc@prison.gov.vn', 'Trần Văn Đốc', 'WARDEN', 'ACTIVE', '0912345678'),
('b0000003-e29b-41d4-a716-446655440003', 'warden_baria', '$2a$12$uI2OLfO2IXFNFLmw38jtt.zhgqOi/d9FD5S64P7aQI91hf30hxftG', 'warden.baria@prison.gov.vn', 'Lê Văn Trưởng', 'WARDEN', 'ACTIVE', '0923456789');

-- Update prison wardens
UPDATE prisons SET warden_id = 'b0000001-e29b-41d4-a716-446655440001' WHERE prison_code = 'TCG-CC';
UPDATE prisons SET warden_id = 'b0000002-e29b-41d4-a716-446655440002' WHERE prison_code = 'TCG-XL';
UPDATE prisons SET warden_id = 'b0000003-e29b-41d4-a716-446655440003' WHERE prison_code = 'TCG-BT';

-- ============================================
-- PRISON ZONE
-- ============================================
INSERT INTO prison_zones (id, prison_id, zone_code, zone_name, zone_type, capacity) VALUES
-- Trại giam Củ Chi (ID: a000...01)
('c0000001-e29b-41d4-a716-446655440101', 'a0000001-e29b-41d4-a716-446655440001', 'ZONE-A', 'Khu A - Mức độ thấp', 'MINIMUM', 150),
('c0000001-e29b-41d4-a716-446655440102', 'a0000001-e29b-41d4-a716-446655440001', 'ZONE-B', 'Khu B - Mức độ trung bình', 'MEDIUM', 200),
('c0000001-e29b-41d4-a716-446655440103', 'a0000001-e29b-41d4-a716-446655440001', 'ZONE-C', 'Khu C - Mức độ cao', 'MAXIMUM', 100),
('c0000001-e29b-41d4-a716-446655440104', 'a0000001-e29b-41d4-a716-446655440001', 'ZONE-D', 'Khu D - Cách ly', 'ISOLATION', 50),

-- Trại giam Xuân Lộc (ID: a000...02)
('c0000002-e29b-41d4-a716-446655440201', 'a0000002-e29b-41d4-a716-446655440002', 'ZONE-A', 'Khu A - Mức độ thấp', 'MINIMUM', 100),
('c0000002-e29b-41d4-a716-446655440202', 'a0000002-e29b-41d4-a716-446655440002', 'ZONE-B', 'Khu B - Mức độ trung bình', 'MEDIUM', 150),
('c0000002-e29b-41d4-a716-446655440203', 'a0000002-e29b-41d4-a716-446655440002', 'ZONE-C', 'Khu C - Mức độ cao', 'MAXIMUM', 50);

-- ============================================
-- USERS - GUARDS (Quản giáo)
-- ============================================
-- Password: 12345
INSERT INTO users (id, username, password_hash, email, full_name, user_type, status, phone_number) VALUES
-- Guards for Củ Chi
('d0000001-e29b-41d4-a716-446655440101', 'guard_cc_zonea', '$2a$12$L04P5ZJs.9AofeaaysmSSu9i8cbmcIuwafR0cGsLdY4KDCfI6bVFS', 'guard.zonea@cuchi.gov.vn', 'Phạm Văn Giáo A', 'GUARD', 'ACTIVE', '0934567890'),
('d0000001-e29b-41d4-a716-446655440102', 'guard_cc_zoneb', '$2a$12$x4XucMl4BsuHNiPzjHR6muoSyakF/WnvvtbK/1D8tsQ/fzwAm5qsi', 'guard.zoneb@cuchi.gov.vn', 'Hoàng Văn Giáo B', 'GUARD', 'ACTIVE', '0945678901'),
('d0000001-e29b-41d4-a716-446655440103', 'guard_cc_zonec', '$2a$12$UMIengNBTELGrmWs5F6fo.Nl08xVfN2zc8LNq7NSCKIfz65YUIIli', 'guard.zonec@cuchi.gov.vn', 'Võ Văn Giáo C', 'GUARD', 'ACTIVE', '0956789012'),
('d0000001-e29b-41d4-a716-446655440104', 'guard_cc_zoned', '$2a$12$Zmy16m1AWD5NCRV7J2qu8OLGQLI2HzneAaclX3YfbY6zhUlzK4QKO', 'guard.zoned@cuchi.gov.vn', 'Đặng Văn Giáo D', 'GUARD', 'ACTIVE', '0967890123'),

-- Guards for Xuân Lộc
('d0000002-e29b-41d4-a716-446655440201', 'guard_xl_zonea', '$2a$12$zHOdL542o4DIQ3/x7/rtw.lBKhj3uQM/WotJKIZoyC2qugI0kA85i', 'guard.zonea@xuanloc.gov.vn', 'Bùi Văn Giáo XL-A', 'GUARD', 'ACTIVE', '0978901234'),
('d0000002-e29b-41d4-a716-446655440202', 'guard_xl_zoneb', '$2a$12$zHOdL542o4DIQ3/x7/rtw.lBKhj3uQM/WotJKIZoyC2qugI0kA85i', 'guard.zoneb@xuanloc.gov.vn', 'Đinh Văn Giáo XL-B', 'GUARD', 'ACTIVE', '0989012345');

-- Assign guards to zones
UPDATE prison_zones SET guard_id = 'd0000001-e29b-41d4-a716-446655440101' WHERE id = 'c0000001-e29b-41d4-a716-446655440101';
UPDATE prison_zones SET guard_id = 'd0000001-e29b-41d4-a716-446655440102' WHERE id = 'c0000001-e29b-41d4-a716-446655440102';
UPDATE prison_zones SET guard_id = 'd0000001-e29b-41d4-a716-446655440103' WHERE id = 'c0000001-e29b-41d4-a716-446655440103';
UPDATE prison_zones SET guard_id = 'd0000001-e29b-41d4-a716-446655440104' WHERE id = 'c0000001-e29b-41d4-a716-446655440104';
UPDATE prison_zones SET guard_id = 'd0000002-e29b-41d4-a716-446655440201' WHERE id = 'c0000002-e29b-41d4-a716-446655440201';
UPDATE prison_zones SET guard_id = 'd0000002-e29b-41d4-a716-446655440202' WHERE id = 'c0000002-e29b-41d4-a716-446655440202';

-- ============================================
-- TABLETS
-- ============================================
INSERT INTO tablets (id, tablet_id, serial_number, model, status, battery_level) VALUES
('e0000001-e29b-41d4-a716-446655440001', 'TABLET-001', 'SN-2025-001', 'Samsung Galaxy Tab A8', 'AVAILABLE', 100),
('e0000001-e29b-41d4-a716-446655440002', 'TABLET-002', 'SN-2025-002', 'Samsung Galaxy Tab A8', 'AVAILABLE', 100),
('e0000001-e29b-41d4-a716-446655440003', 'TABLET-003', 'SN-2025-003', 'Samsung Galaxy Tab A8', 'AVAILABLE', 100),
('e0000001-e29b-41d4-a716-446655440004', 'TABLET-004', 'SN-2025-004', 'Samsung Galaxy Tab A8', 'AVAILABLE', 100),
('e0000001-e29b-41d4-a716-446655440005', 'TABLET-005', 'SN-2025-005', 'Samsung Galaxy Tab A8', 'AVAILABLE', 100);

-- ============================================
-- USERS - PRISONERS (Link to existing prisoners)
-- ============================================
-- Password: prisoner123
-- Fingerprint data is simulated
INSERT INTO users (id, username, password_hash, full_name, user_type, status, fingerprint_data) VALUES
('f0000001-e29b-41d4-a716-446655440001', 'prisoner_p2025001', '$2a$10$pGZqK5YqXqXqXqXqXqXqXOqXqXqXqXqXqXqXqXqXqXqXqXqXqXqXq', 'Nguyễn Văn An', 'PRISONER', 'ACTIVE', 'FP_DATA_ENCRYPTED_001'),
('f0000001-e29b-41d4-a716-446655440002', 'prisoner_p2025002', '$2a$10$pGZqK5YqXqXqXqXqXqXqXOqXqXqXqXqXqXqXqXqXqXqXqXqXqXqXq', 'Trần Thị Bình', 'PRISONER', 'ACTIVE', 'FP_DATA_ENCRYPTED_002'),
('f0000001-e29b-41d4-a716-446655440003', 'prisoner_p2025003', '$2a$10$pGZqK5YqXqXqXqXqXqXqXOqXqXqXqXqXqXqXqXqXqXqXqXqXqXqXq', 'Lê Văn Cường', 'PRISONER', 'ACTIVE', 'FP_DATA_ENCRYPTED_003'),
('f0000001-e29b-41d4-a716-446655440004', 'prisoner_p2025004', '$2a$10$pGZqK5YqXqXqXqXqXqXqXOqXqXqXqXqXqXqXqXqXqXqXqXqXqXqXq', 'Phạm Thị Dung', 'PRISONER', 'ACTIVE', 'FP_DATA_ENCRYPTED_004'),
('f0000001-e29b-41d4-a716-446655440005', 'prisoner_p2025005', '$2a$10$pGZqK5YqXqXqXqXqXqXqXOqXqXqXqXqXqXqXqXqXqXqXqXqXqXqXq', 'Hoàng Văn Em', 'PRISONER', 'ACTIVE', 'FP_DATA_ENCRYPTED_005');

-- Link prisoners to users, prisons, zones, and tablets
-- Note: Assuming prisoners table exists. We update fields with VALID UUIDs.
UPDATE prisoners SET 
    user_id = 'f0000001-e29b-41d4-a716-446655440001',
    prison_id = 'a0000001-e29b-41d4-a716-446655440001',
    zone_id = 'c0000001-e29b-41d4-a716-446655440102',
    tablet_id = 'TABLET-001',
    tablet_assigned_date = CURRENT_TIMESTAMP
WHERE prisoner_number = 'P2025-001';

UPDATE prisoners SET 
    user_id = 'f0000001-e29b-41d4-a716-446655440002',
    prison_id = 'a0000001-e29b-41d4-a716-446655440001',
    zone_id = 'c0000001-e29b-41d4-a716-446655440101',
    tablet_id = 'TABLET-002',
    tablet_assigned_date = CURRENT_TIMESTAMP
WHERE prisoner_number = 'P2025-002';

UPDATE prisoners SET 
    user_id = 'f0000001-e29b-41d4-a716-446655440003',
    prison_id = 'a0000001-e29b-41d4-a716-446655440001',
    zone_id = 'c0000001-e29b-41d4-a716-446655440103',
    tablet_id = 'TABLET-003',
    tablet_assigned_date = CURRENT_TIMESTAMP
WHERE prisoner_number = 'P2025-003';

UPDATE prisoners SET 
    user_id = 'f0000001-e29b-41d4-a716-446655440004',
    prison_id = 'a0000002-e29b-41d4-a716-446655440002',
    zone_id = 'c0000002-e29b-41d4-a716-446655440201',
    tablet_id = 'TABLET-004',
    tablet_assigned_date = CURRENT_TIMESTAMP
WHERE prisoner_number = 'P2025-004';

UPDATE prisoners SET 
    user_id = 'f0000001-e29b-41d4-a716-446655440005',
    prison_id = 'a0000001-e29b-41d4-a716-446655440001',
    zone_id = 'c0000001-e29b-41d4-a716-446655440102',
    tablet_id = 'TABLET-005',
    tablet_assigned_date = CURRENT_TIMESTAMP
WHERE prisoner_number = 'P2025-005';

-- Update tablets assignment
-- NOTE: Assuming the existing prisoners records (PK) are mapped to these UUIDs:
-- P001 -> '550e8400-e29b-41d4-a716-446655440001'
-- P002 -> '550e8400-e29b-41d4-a716-446655440002'
-- ...
-- This ensures 'assigned_prisoner_id' matches 'prisoner_id' in attendance records
UPDATE tablets SET assigned_prisoner_id = '550e8400-e29b-41d4-a716-446655440001', assigned_date = CURRENT_TIMESTAMP, status = 'ASSIGNED' WHERE tablet_id = 'TABLET-001';
UPDATE tablets SET assigned_prisoner_id = '550e8400-e29b-41d4-a716-446655440002', assigned_date = CURRENT_TIMESTAMP, status = 'ASSIGNED' WHERE tablet_id = 'TABLET-002';
UPDATE tablets SET assigned_prisoner_id = '550e8400-e29b-41d4-a716-446655440003', assigned_date = CURRENT_TIMESTAMP, status = 'ASSIGNED' WHERE tablet_id = 'TABLET-003';
UPDATE tablets SET assigned_prisoner_id = '550e8400-e29b-41d4-a716-446655440004', assigned_date = CURRENT_TIMESTAMP, status = 'ASSIGNED' WHERE tablet_id = 'TABLET-004';
UPDATE tablets SET assigned_prisoner_id = '550e8400-e29b-41d4-a716-446655440005', assigned_date = CURRENT_TIMESTAMP, status = 'ASSIGNED' WHERE tablet_id = 'TABLET-005';

-- Update prison populations
UPDATE prisons SET current_population = 4 WHERE prison_code = 'TCG-CC';
UPDATE prisons SET current_population = 1 WHERE prison_code = 'TCG-XL';

UPDATE prison_zones SET current_population = 1 WHERE id = 'c0000001-e29b-41d4-a716-446655440101'; -- Zone A: 1 prisoner
UPDATE prison_zones SET current_population = 2 WHERE id = 'c0000001-e29b-41d4-a716-446655440102'; -- Zone B: 2 prisoners
UPDATE prison_zones SET current_population = 1 WHERE id = 'c0000001-e29b-41d4-a716-446655440103'; -- Zone C: 1 prisoner
UPDATE prison_zones SET current_population = 1 WHERE id = 'c0000002-e29b-41d4-a716-446655440201'; -- XL Zone A: 1 prisoner

-- ============================================
-- SAMPLE ATTENDANCE RECORDS (Today)
-- ============================================
-- Note: These IDs must match the Prisoner PKs used in tablets table above
INSERT INTO attendance_records (prisoner_id, attendance_date, expected_check_in_time, expected_check_out_time, status) VALUES
('550e8400-e29b-41d4-a716-446655440001', CURRENT_DATE, CURRENT_DATE + TIME '06:00:00', CURRENT_DATE + TIME '18:00:00', 'PENDING'),
('550e8400-e29b-41d4-a716-446655440002', CURRENT_DATE, CURRENT_DATE + TIME '06:00:00', CURRENT_DATE + TIME '18:00:00', 'PENDING'),
('550e8400-e29b-41d4-a716-446655440003', CURRENT_DATE, CURRENT_DATE + TIME '06:00:00', CURRENT_DATE + TIME '18:00:00', 'PENDING'),
('550e8400-e29b-41d4-a716-446655440005', CURRENT_DATE, CURRENT_DATE + TIME '06:00:00', CURRENT_DATE + TIME '18:00:00', 'PENDING');

-- ============================================
-- COMMENTS
-- ============================================
COMMENT ON TABLE users IS 'Seed data: 3 wardens, 6 guards, 5 prisoners';
COMMENT ON TABLE prisons IS 'Seed data: 3 prisons (Củ Chi, Xuân Lộc, Bà Rịa)';
COMMENT ON TABLE prison_zones IS 'Seed data: 7 zones across prisons';
COMMENT ON TABLE tablets IS 'Seed data: 5 tablets assigned to prisoners';
COMMENT ON TABLE attendance_records IS 'Seed data: Today attendance records for active prisoners';