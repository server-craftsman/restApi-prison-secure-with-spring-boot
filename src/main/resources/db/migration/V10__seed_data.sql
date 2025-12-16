-- V10: Seed Data for Testing and Development
-- This migration adds sample data for all modules

-- ============================================
-- PRISONERS
-- ============================================
INSERT INTO prisoners (id, prisoner_number, first_name, last_name, date_of_birth, gender, nationality, status, admission_date, created_at, updated_at, version) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'P2025-001', 'Nguyễn Văn', 'An', '1985-03-15', 'MALE', 'Vietnamese', 'ACTIVE', '2025-01-10 08:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('550e8400-e29b-41d4-a716-446655440002', 'P2025-002', 'Trần Thị', 'Bình', '1990-07-22', 'FEMALE', 'Vietnamese', 'ACTIVE', '2025-01-15 09:30:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('550e8400-e29b-41d4-a716-446655440003', 'P2025-003', 'Lê Văn', 'Cường', '1982-11-08', 'MALE', 'Vietnamese', 'ACTIVE', '2025-01-20 10:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('550e8400-e29b-41d4-a716-446655440004', 'P2025-004', 'Phạm Thị', 'Dung', '1995-05-30', 'FEMALE', 'Vietnamese', 'RELEASED', '2024-06-01 08:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('550e8400-e29b-41d4-a716-446655440005', 'P2025-005', 'Hoàng Văn', 'Em', '1988-09-12', 'MALE', 'Vietnamese', 'ACTIVE', '2025-02-01 11:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- ============================================
-- BOOKINGS
-- ============================================
INSERT INTO bookings (id, prisoner_id, booking_number, booking_date, booking_type, booking_status, arresting_agency, arrest_location, arrest_date, created_at, updated_at, created_by, updated_by, version) VALUES
('650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', 'BK2025-001', '2025-01-10 08:00:00', 'NEW_ARREST', 'ACTIVE', 'Công an TP.HCM', 'Quận 1, TP.HCM', '2025-01-09 20:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', 0),
('650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440002', 'BK2025-002', '2025-01-15 09:30:00', 'COURT_ORDER', 'ACTIVE', 'Tòa án Nhân dân TP.HCM', 'Tòa án Quận 3', '2025-01-14 14:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', 0),
('650e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440003', 'BK2025-003', '2025-01-20 10:00:00', 'WARRANT', 'ACTIVE', 'Công an Hà Nội', 'Quận Ba Đình, Hà Nội', '2025-01-19 15:30:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', 0),
('650e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440004', 'BK2024-156', '2024-06-01 08:00:00', 'NEW_ARREST', 'RELEASED', 'Công an Đà Nẵng', 'Quận Hải Châu, Đà Nẵng', '2024-05-31 18:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', 0),
('650e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440005', 'BK2025-004', '2025-02-01 11:00:00', 'TRANSFER', 'ACTIVE', 'Trại giam Xuân Lộc', 'Đồng Nai', '2025-01-31 16:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', 0);

-- ============================================
-- MEDICAL RECORDS
-- ============================================
INSERT INTO medical_records (id, prisoner_id, record_date, record_type, diagnosis, treatment, severity, medical_staff, notes, follow_up_required, follow_up_date, created_at, updated_at, created_by, updated_by, version) VALUES
('750e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', '2025-01-10 10:00:00', 'ROUTINE_CHECKUP', 'Khám sức khỏe ban đầu', 'Không cần điều trị đặc biệt', 'LOW', 'BS. Nguyễn Thị Mai', 'Sức khỏe tốt, không có bệnh mãn tính', true, '2025-04-10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'doctor1', 'doctor1', 0),
('750e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440002', '2025-01-15 11:00:00', 'GENERAL', 'Cao huyết áp', 'Thuốc hạ huyết áp, chế độ ăn ít muối', 'MEDIUM', 'BS. Trần Văn Hùng', 'Cần theo dõi huyết áp hàng tuần', true, '2025-02-15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'doctor2', 'doctor2', 0),
('750e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440003', '2025-01-20 14:00:00', 'GENERAL', 'Viêm dạ dày', 'Thuốc kháng acid, chế độ ăn nhẹ', 'LOW', 'BS. Lê Thị Hoa', 'Triệu chứng đau bụng, khó tiêu', false, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'doctor1', 'doctor1', 0),
('750e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440005', '2025-02-01 13:00:00', 'GENERAL', 'Đau đầu mãn tính', 'Thuốc giảm đau, nghỉ ngơi', 'MEDIUM', 'BS. Phạm Văn Nam', 'Stress, cần theo dõi tâm lý', true, '2025-03-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'doctor2', 'doctor2', 0);

-- ============================================
-- PRESCRIPTIONS
-- ============================================
INSERT INTO prescriptions (id, prisoner_id, medical_record_id, medication_name, dosage, frequency, start_date, end_date, prescribing_doctor, status, pharmacy_notes, created_at, updated_at, created_by, updated_by, version) VALUES
('850e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440002', '750e8400-e29b-41d4-a716-446655440002', 'Amlodipine', '5mg', 'Ngày 1 viên', '2025-01-15', '2025-04-15', 'BS. Trần Văn Hùng', 'ACTIVE', 'Uống vào buổi sáng sau bữa ăn', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'doctor2', 'doctor2', 0),
('850e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440003', '750e8400-e29b-41d4-a716-446655440003', 'Omeprazole', '20mg', 'Ngày 2 lần', '2025-01-20', '2025-02-20', 'BS. Lê Thị Hoa', 'ACTIVE', 'Uống trước bữa ăn 30 phút', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'doctor1', 'doctor1', 0),
('850e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440005', '750e8400-e29b-41d4-a716-446655440004', 'Paracetamol', '500mg', 'Khi đau đầu', '2025-02-01', '2025-03-01', 'BS. Phạm Văn Nam', 'ACTIVE', 'Không quá 4g/ngày', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'doctor2', 'doctor2', 0);

-- ============================================
-- SCHEDULES
-- ============================================
INSERT INTO schedules (id, prisoner_id, schedule_type, title, scheduled_date, location, description, status, created_by, created_at, updated_at, version) VALUES
('950e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', 'COURT', 'Phiên tòa xét xử sơ thẩm', '2025-03-15 09:00:00', 'Tòa án Nhân dân TP.HCM', 'Chuẩn bị hồ sơ pháp lý', 'SCHEDULED', 'admin', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('950e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440002', 'MEDICAL', 'Tái khám cao huyết áp', '2025-02-15 10:00:00', 'Phòng khám trại giam', 'Đo huyết áp và cân nặng', 'SCHEDULED', 'doctor2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('950e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440003', 'COURT', 'Phiên tòa phúc thẩm', '2025-02-28 14:00:00', 'Tòa án Nhân dân Hà Nội', 'Liên hệ luật sư bào chữa', 'SCHEDULED', 'admin', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('950e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440005', 'MEDICAL', 'Tái khám đau đầu', '2025-03-01 11:00:00', 'Phòng khám trại giam', 'Đánh giá tình trạng stress', 'SCHEDULED', 'doctor2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- ============================================
-- WORKFLOWS
-- ============================================
INSERT INTO workflows (id, workflow_type, workflow_name, description, created_at, updated_at, created_by, updated_by, version) VALUES
('a50e8400-e29b-41d4-a716-446655440001', 'RELEASE', 'Quy trình thả tù', 'Quy trình xử lý thả tù nhân', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', 0),
('a50e8400-e29b-41d4-a716-446655440002', 'TRANSFER', 'Quy trình chuyển trại', 'Quy trình chuyển tù nhân sang trại khác', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', 0),
('a50e8400-e29b-41d4-a716-446655440003', 'OTHER', 'Quy trình điều trị đặc biệt', 'Quy trình điều trị bệnh nặng', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', 0);

-- ============================================
-- WORKFLOW INSTANCES
-- ============================================
INSERT INTO workflow_instances (id, workflow_id, prisoner_id, reference_id, reference_type, status, current_step, total_steps, priority, started_at, initiated_by, metadata, created_at, updated_at, created_by, updated_by, version) VALUES
('b50e8400-e29b-41d4-a716-446655440001', 'a50e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440004', '650e8400-e29b-41d4-a716-446655440004', 'BOOKING', 'COMPLETED', 5, 5, 'NORMAL', '2025-01-01 08:00:00', 'admin', '{"reason": "Hết hạn tù"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', 0),
('b50e8400-e29b-41d4-a716-446655440002', 'a50e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440005', '650e8400-e29b-41d4-a716-446655440005', 'BOOKING', 'IN_PROGRESS', 2, 4, 'HIGH', '2025-01-31 10:00:00', 'admin', '{"destination": "Trại giam Xuân Lộc"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', 0);

-- ============================================
-- VISITORS (Sample data for visit management)
-- ============================================
INSERT INTO visitors (id, first_name, last_name, identification_type, identification_number, contact_number, address, created_at, updated_at, created_by, updated_by, version) VALUES
('c50e8400-e29b-41d4-a716-446655440001', 'Nguyễn Thị', 'Lan', 'NATIONAL_ID', '079085001234', '0901234567', '123 Nguyễn Huệ, Q1, TP.HCM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', 0),
('c50e8400-e29b-41d4-a716-446655440002', 'Trần Văn', 'Minh', 'NATIONAL_ID', '079090005678', '0912345678', '456 Lê Lợi, Q3, TP.HCM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', 0),
('c50e8400-e29b-41d4-a716-446655440003', 'Lê Thị', 'Nga', 'NATIONAL_ID', '079075009012', '0923456789', '789 Trần Hưng Đạo, Hà Nội', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', 0);

-- ============================================
-- COMMENTS
-- ============================================
COMMENT ON TABLE prisoners IS 'Seed data: 5 tù nhân mẫu với các trạng thái khác nhau';
COMMENT ON TABLE bookings IS 'Seed data: 5 hồ sơ tiếp nhận tương ứng';
COMMENT ON TABLE medical_records IS 'Seed data: 4 hồ sơ y tế với các bệnh khác nhau';
COMMENT ON TABLE prescriptions IS 'Seed data: 3 đơn thuốc đang hoạt động';
COMMENT ON TABLE schedules IS 'Seed data: 4 lịch trình sắp tới';
COMMENT ON TABLE workflows IS 'Seed data: 3 quy trình mẫu';
COMMENT ON TABLE workflow_instances IS 'Seed data: 2 instance quy trình';
COMMENT ON TABLE visitors IS 'Seed data: 3 người thăm nuôi đã được phê duyệt';
