-- V11: Authentication and Multi-Prison Schema
-- Creates users, prisons, zones, and authentication tables

-- ============================================
-- USERS TABLE
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
-- PRISONS TABLE (Multi-prison support)
-- ============================================
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
-- PRISON ZONES (Khu vực trong trại)
-- ============================================
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
-- UPDATE PRISONERS TABLE
-- ============================================
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
-- ATTENDANCE RECORDS (Check-in/Check-out)
-- ============================================
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
-- ESCAPE REQUESTS (Yêu cầu trốn tù)
-- ============================================
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
-- ESCAPE HISTORY (Lịch sử trốn tù thực tế)
-- ============================================
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
-- TABLETS (Quản lý tablet của tù nhân)
-- ============================================
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
-- COMMENTS
-- ============================================
COMMENT ON TABLE users IS 'Người dùng hệ thống: Tù nhân, Quản giáo, Quản đốc';
COMMENT ON TABLE prisons IS 'Danh sách các nhà tù trong hệ thống';
COMMENT ON TABLE prison_zones IS 'Các khu vực trong nhà tù';
COMMENT ON TABLE attendance_records IS 'Lịch sử check-in/check-out hàng ngày của tù nhân';
COMMENT ON TABLE escape_requests IS 'Yêu cầu trốn tù từ tù nhân (cần phê duyệt 2 cấp)';
COMMENT ON TABLE escape_history IS 'Lịch sử trốn tù thực tế';
COMMENT ON TABLE tablets IS 'Quản lý tablet được cấp cho tù nhân';

COMMENT ON COLUMN attendance_records.vibration_level IS '0=không rung, 1=nhẹ (5-15 phút), 2=trung bình (15-30 phút), 3=mạnh (30+ phút)';
COMMENT ON COLUMN escape_requests.status IS 'Trạng thái: PENDING -> GUARD_APPROVED -> WARDEN_APPROVED -> EXECUTED';
