-- V11: Authentication and Multi-Prison Schema
-- Tạo schema xác thực và hỗ trợ đa nhà tù

-- ============================================
-- BẢNG: users
-- MỤC ĐÍCH: Lưu trữ thông tin người dùng của hệ thống
-- MÔ TẢ: Quản lý tài khoản người dùng bao gồm:
--        - Loại người dùng (tù nhân, quản giáo, quản đốc)
--        - Tên người dùng, mật khẩu hash, email, tên đầy đủ, số điện thoại
--        - Dữ liệu sinh trắc học (vân tay cho tù nhân)
--        - Trạng thái (hoạt động, không hoạt động, bị tạm dừng)
-- ============================================
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    full_name VARCHAR(100) NOT NULL,
    user_type VARCHAR(20) NOT NULL, -- PRISONER, GUARD, WARDEN
    status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, INACTIVE, SUSPENDED
    fingerprint_data TEXT, -- Biometric data for prisoners
    phone_number VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    version INTEGER DEFAULT 0,
    CONSTRAINT chk_user_type CHECK (user_type IN ('PRISONER', 'GUARD', 'WARDEN')),
    CONSTRAINT chk_user_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED'))
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_user_type ON users(user_type);
CREATE INDEX idx_users_status ON users(status);

-- ============================================
-- BẢNG: prisons
-- MỤC ĐÍCH: Lưu trữ thông tin các nhà tù trong hệ thống (hỗ trợ đa nhà tù)
-- MÔ TẢ: Quản lý nhà tù bao gồm:
--        - Mã nhà tù, tên, vị trí, địa chỉ
--        - Sức chứa, dân số hiện tại
--        - Quản đốc (tham chiếu đến users)
--        - Trạng thái (hoạt động, không hoạt động, bảo trì), ngày thành lập
-- ============================================
-- PRISONS TABLE (Multi-prison support)
CREATE TABLE prisons (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    prison_code VARCHAR(20) UNIQUE NOT NULL,
    prison_name VARCHAR(100) NOT NULL,
    location VARCHAR(200),
    address TEXT,
    capacity INTEGER NOT NULL,
    current_population INTEGER DEFAULT 0,
    warden_id UUID REFERENCES users(id),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    established_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    version INTEGER DEFAULT 0,
    CONSTRAINT chk_prison_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'MAINTENANCE'))
);

CREATE INDEX idx_prisons_code ON prisons(prison_code);
CREATE INDEX idx_prisons_warden ON prisons(warden_id);

-- ============================================
-- BẢNG: prison_zones
-- MỤC ĐÍCH: Lưu trữ thông tin các khu vực bên trong nhà tù
-- MÔ TẢ: Quản lý khu vực bao gồm:
--        - Mã khu vực, tên khu vực trong nhà tù cụ thể
--        - Loại khu vực (tối thiểu, trung bình, tối đa, cách ly)
--        - Quản giáo phụ trách, sức chứa, dân số hiện tại
--        - Trạng thái (hoạt động/không hoạt động)
-- ============================================
-- PRISON ZONES (Khu vực trong trại)
CREATE TABLE prison_zones (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    prison_id UUID NOT NULL REFERENCES prisons(id) ON DELETE CASCADE,
    zone_code VARCHAR(20) NOT NULL,
    zone_name VARCHAR(100) NOT NULL,
    zone_type VARCHAR(20), -- MINIMUM, MEDIUM, MAXIMUM, ISOLATION
    guard_id UUID REFERENCES users(id),
    capacity INTEGER NOT NULL,
    current_population INTEGER DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 0,
    CONSTRAINT uk_prison_zone_code UNIQUE (prison_id, zone_code),
    CONSTRAINT chk_zone_type CHECK (zone_type IN ('MINIMUM', 'MEDIUM', 'MAXIMUM', 'ISOLATION'))
);

CREATE INDEX idx_zones_prison ON prison_zones(prison_id);
CREATE INDEX idx_zones_guard ON prison_zones(guard_id);

-- ============================================
-- CẬP NHẬT BẢNG: prisoners
-- MỤC ĐÍCH: Thêm cột liên kết đến nhà tù, khu vực, người dùng, tablet
-- MÔ TẢ: Cột được thêm bao gồm:
--        - prison_id: Nhà tù tù nhân đang giam giữ
--        - zone_id: Khu vực/khối tù nhân đang nằm
--        - user_id: Tài khoản người dùng tương ứng của tù nhân
--        - tablet_id: Tablet được cấp cho tù nhân
--        - tablet_assigned_date: Ngày cấp tablet
-- ============================================
-- UPDATE PRISONERS TABLE
ALTER TABLE prisoners ADD COLUMN IF NOT EXISTS prison_id UUID REFERENCES prisons(id);
ALTER TABLE prisoners ADD COLUMN IF NOT EXISTS zone_id UUID REFERENCES prison_zones(id);
ALTER TABLE prisoners ADD COLUMN IF NOT EXISTS user_id UUID REFERENCES users(id);
ALTER TABLE prisoners ADD COLUMN IF NOT EXISTS tablet_id VARCHAR(50) UNIQUE;
ALTER TABLE prisoners ADD COLUMN IF NOT EXISTS tablet_assigned_date TIMESTAMP;

CREATE INDEX idx_prisoners_prison ON prisoners(prison_id);
CREATE INDEX idx_prisoners_zone ON prisoners(zone_id);
CREATE INDEX idx_prisoners_user ON prisoners(user_id);
CREATE INDEX idx_prisoners_tablet ON prisoners(tablet_id);

-- ============================================
-- BẢNG: attendance_records
-- MỤC ĐÍCH: Ghi nhật ký check-in/check-out hàng ngày của tù nhân
-- MÔ TẢ: Quản lý điểm danh bao gồm:
--        - Ngày điểm danh, giờ check-in thực tế, giờ check-out thực tế
--        - Giờ check-in mong đợi, giờ check-out mong đợi
--        - Trạng thái (chờ xử lý, đúng giờ, trễ, vắng mặt, hoàn thành)
--        - Phút trễ, cảnh báo rung (mức độ: 0-3, có rung hay không)
-- ============================================
-- ATTENDANCE RECORDS (Check-in/Check-out)
CREATE TABLE attendance_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    prisoner_id VARCHAR(36) NOT NULL REFERENCES prisoners(id) ON DELETE CASCADE,
    attendance_date DATE NOT NULL,
    check_in_time TIMESTAMP,
    check_out_time TIMESTAMP,
    expected_check_in_time TIMESTAMP NOT NULL,
    expected_check_out_time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING, ON_TIME, LATE, MISSED, COMPLETED
    late_minutes INTEGER DEFAULT 0,
    vibration_level INTEGER DEFAULT 0, -- 0=none, 1=low, 2=medium, 3=high
    vibration_triggered BOOLEAN DEFAULT FALSE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 0,
    CONSTRAINT chk_attendance_status CHECK (status IN ('PENDING', 'ON_TIME', 'LATE', 'MISSED', 'COMPLETED')),
    CONSTRAINT chk_vibration_level CHECK (vibration_level BETWEEN 0 AND 3),
    CONSTRAINT uk_attendance_prisoner_date UNIQUE (prisoner_id, attendance_date)
);

CREATE INDEX idx_attendance_prisoner ON attendance_records(prisoner_id);
CREATE INDEX idx_attendance_date ON attendance_records(attendance_date);
CREATE INDEX idx_attendance_status ON attendance_records(status);

-- ============================================
-- BẢNG: escape_requests
-- MỤC ĐÍCH: Lưu trữ yêu cầu trốn tù từ tù nhân (cần xét duyệt 2 cấp)
-- MÔ TẢ: Quản lý yêu cầu trốn tù bao gồm:
--        - Lý do yêu cầu, ngày dự kiến trốn tù
--        - Trạng thái (chờ xử lý -> phê duyệt quản giáo -> phê duyệt quản đốc -> thực hiện)
--        - Phê duyệt từ quản giáo (ngày, ghi chú, ID người phê duyệt)
--        - Phê duyệt từ quản đốc (ngày, ghi chú, ID người phê duyệt)
--        - Thực hiện (ngày, ghi chú)
-- ============================================
-- ESCAPE REQUESTS (Yêu cầu trốn tù)
CREATE TABLE escape_requests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    prisoner_id VARCHAR(36) NOT NULL REFERENCES prisoners(id) ON DELETE CASCADE,
    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reason TEXT NOT NULL,
    planned_escape_date DATE,
    status VARCHAR(30) DEFAULT 'PENDING', 
    -- PENDING, GUARD_APPROVED, GUARD_REJECTED, WARDEN_APPROVED, WARDEN_REJECTED, EXECUTED
    
    -- Guard approval
    guard_approval_date TIMESTAMP,
    guard_approval_notes TEXT,
    approved_by_guard UUID REFERENCES users(id),
    
    -- Warden approval
    warden_approval_date TIMESTAMP,
    warden_approval_notes TEXT,
    approved_by_warden UUID REFERENCES users(id),
    
    -- Execution
    execution_date TIMESTAMP,
    execution_notes TEXT,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 0,
    CONSTRAINT chk_escape_status CHECK (status IN (
        'PENDING', 'GUARD_APPROVED', 'GUARD_REJECTED', 
        'WARDEN_APPROVED', 'WARDEN_REJECTED', 'EXECUTED'
    ))
);

CREATE INDEX idx_escape_prisoner ON escape_requests(prisoner_id);
CREATE INDEX idx_escape_status ON escape_requests(status);
CREATE INDEX idx_escape_guard ON escape_requests(approved_by_guard);
CREATE INDEX idx_escape_warden ON escape_requests(approved_by_warden);

-- ============================================
-- BẢNG: escape_history
-- MỤC ĐÍCH: Lưu trữ lịch sử trốn tù thực tế
-- MÔ TẢ: Quản lý lịch sử trốn tù bao gồm:
--        - Liên kết đến yêu cầu trốn tù (nếu có)
--        - Ngày trốn tù, ngày bị bắt lại
--        - Trạng thái (đã trốn tù, bị bắt lại, đang lẩn trốn, tử vong)
--        - Phương thức trốn, nơi bị bắt lại, số ngày đang lẩn trốn
-- ============================================
-- ESCAPE HISTORY (Lịch sử trốn tù thực tế)
CREATE TABLE escape_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    prisoner_id VARCHAR(36) NOT NULL REFERENCES prisoners(id) ON DELETE CASCADE,
    escape_request_id UUID REFERENCES escape_requests(id),
    escape_date TIMESTAMP NOT NULL,
    recapture_date TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'ESCAPED', -- ESCAPED, RECAPTURED, AT_LARGE, DECEASED
    escape_method TEXT,
    recapture_location VARCHAR(200),
    days_at_large INTEGER,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 0,
    CONSTRAINT chk_escape_history_status CHECK (status IN ('ESCAPED', 'RECAPTURED', 'AT_LARGE', 'DECEASED'))
);

CREATE INDEX idx_escape_history_prisoner ON escape_history(prisoner_id);
CREATE INDEX idx_escape_history_status ON escape_history(status);
CREATE INDEX idx_escape_history_date ON escape_history(escape_date);

-- ============================================
-- BẢNG: tablets
-- MỤC ĐÍCH: Quản lý tablet được cấp cho tù nhân
-- MÔ TẢ: Quản lý thiết bị tablet bao gồm:
--        - ID tablet, serial number (duy nhất), model
--        - Tù nhân được giao, ngày giao
--        - Trạng thái (sẵn sàng, đã giao, bảo trì, bị hỏng)
--        - Thời gian đồng bộ cuối cùng, mức pin
-- ============================================
-- TABLETS (Quản lý tablet của tù nhân)
CREATE TABLE tablets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tablet_id VARCHAR(50) UNIQUE NOT NULL,
    serial_number VARCHAR(100) UNIQUE NOT NULL,
    model VARCHAR(50),
    assigned_prisoner_id VARCHAR(36) REFERENCES prisoners(id),
    assigned_date TIMESTAMP,
    status VARCHAR(20) DEFAULT 'AVAILABLE', -- AVAILABLE, ASSIGNED, MAINTENANCE, BROKEN
    last_sync_time TIMESTAMP,
    battery_level INTEGER,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 0,
    CONSTRAINT chk_tablet_status CHECK (status IN ('AVAILABLE', 'ASSIGNED', 'MAINTENANCE', 'BROKEN')),
    CONSTRAINT chk_battery_level CHECK (battery_level BETWEEN 0 AND 100)
);

CREATE INDEX idx_tablets_prisoner ON tablets(assigned_prisoner_id);
CREATE INDEX idx_tablets_status ON tablets(status);

-- ============================================
-- GHI CHÚ (Comments) - Mô tả bảng và cột
-- ============================================
-- COMMENTS
COMMENT ON TABLE users IS 'Người dùng hệ thống: Tù nhân, Quản giáo, Quản đốc';
COMMENT ON TABLE prisons IS 'Danh sách các nhà tù trong hệ thống (hỗ trợ quản lý đa nhà tù)';
COMMENT ON TABLE prison_zones IS 'Các khu vực/khối trong nhà tù (tối thiểu, trung bình, tối đa, cách ly)';
COMMENT ON TABLE attendance_records IS 'Lịch sử check-in/check-out hàng ngày của tù nhân với cảnh báo rung';
COMMENT ON TABLE escape_requests IS 'Yêu cầu trốn tù từ tù nhân (cần phê duyệt 2 cấp: quản giáo và quản đốc)';
COMMENT ON TABLE escape_history IS 'Lịch sử trốn tù thực tế và bị bắt lại của tù nhân';
COMMENT ON TABLE tablets IS 'Quản lý tablet được cấp cho tù nhân để giao tiếp/hoạt động';

COMMENT ON COLUMN attendance_records.vibration_level IS '0=không rung, 1=nhẹ (5-15 phút), 2=trung bình (15-30 phút), 3=mạnh (30+ phút)';
COMMENT ON COLUMN escape_requests.status IS 'Quy trình phê duyệt: PENDING -> GUARD_APPROVED -> WARDEN_APPROVED -> EXECUTED';
