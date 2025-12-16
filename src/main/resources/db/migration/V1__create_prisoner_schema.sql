-- ============================================
-- V1: Create Prisoner Schema
-- Tạo schema quản lý tù nhân
-- ============================================

-- ============================================
-- BẢNG: prisoners
-- MỤC ĐÍCH: Lưu trữ thông tin cơ bản và toàn diện của tù nhân
-- MÔ TẢ: Bảng chính quản lý hồ sơ tù nhân bao gồm:
--        - Thông tin nhân khẩu học (họ tên, ngày sinh, giới tính, quốc tịch)
--        - Đặc điểm vật lý (chiều cao, cân nặng, màu mắt, sáo, hình xam)
--        - Trạng thái giam giữ (đang giam, tạm thả, đã thả, trốn)
--        - Vị trí hiện tại (trại giam, khu, phòng giam)
-- ============================================
CREATE TABLE IF NOT EXISTS prisoners (
    id VARCHAR(36) PRIMARY KEY,
    prisoner_number VARCHAR(50) NOT NULL UNIQUE,
    
    -- Demographic Information
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    date_of_birth DATE NOT NULL,
    gender VARCHAR(20) NOT NULL CHECK (gender IN ('MALE', 'FEMALE', 'OTHER', 'UNKNOWN')),
    nationality VARCHAR(100),
    place_of_birth VARCHAR(200),
    national_id_number VARCHAR(50),
    
    -- Physical Description
    height_cm INTEGER CHECK (height_cm >= 50 AND height_cm <= 300),
    weight_kg INTEGER CHECK (weight_kg >= 10 AND weight_kg <= 500),
    eye_color VARCHAR(50),
    hair_color VARCHAR(50),
    blood_type VARCHAR(10),
    distinctive_marks TEXT,  -- JSON array
    scars TEXT,             -- JSON array
    tattoos TEXT,           -- JSON array
    
    -- Status Information
    status VARCHAR(30) NOT NULL CHECK (status IN (
        'PENDING_ADMISSION', 'ACTIVE', 'TEMPORARY_RELEASE',
        'ON_PAROLE', 'RELEASED', 'TRANSFERRED', 'DECEASED', 'ESCAPED'
    )),
    admission_date TIMESTAMP,
    release_date TIMESTAMP,
    
    -- Location Information
    assigned_facility VARCHAR(100),
    current_block VARCHAR(50),
    current_cell VARCHAR(50),
    
    -- Audit Fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT valid_date_range CHECK (
        admission_date IS NULL OR release_date IS NULL OR release_date >= admission_date
    )
);

-- Create indexes for performance
CREATE INDEX idx_prisoner_number ON prisoners(prisoner_number);
CREATE INDEX idx_national_id ON prisoners(national_id_number);
CREATE INDEX idx_status ON prisoners(status);
CREATE INDEX idx_facility ON prisoners(assigned_facility);
CREATE INDEX idx_admission_date ON prisoners(admission_date);
CREATE INDEX idx_name_search ON prisoners(first_name, last_name);

-- ============================================
-- BẢNG: biometric_data
-- MỤC ĐÍCH: ưu trữ dữ liệu sinh trắc học để nhận diện tù nhân
-- MÔ TẢ: Chứa các mẫu sinh trắc học (vân tay, mống mắt, khuôn mặt, DNA)
--        tuân thủ chuẩn FBI/NIST để xác thực danh tính tù nhân
-- ============================================
CREATE TABLE IF NOT EXISTS biometric_data (
    id VARCHAR(36) PRIMARY KEY,
    prisoner_id VARCHAR(36) NOT NULL,
    
    biometric_type VARCHAR(50) NOT NULL CHECK (biometric_type IN (
        'FINGERPRINT', 'PALM_VEIN', 'FINGER_VEIN', 'IRIS', 'FACIAL', 'DNA'
    )),
    template_data BYTEA NOT NULL,
    format VARCHAR(50) NOT NULL,
    quality_score INTEGER,
    quality VARCHAR(20) CHECK (quality IN ('EXCELLENT', 'GOOD', 'FAIR', 'POOR', 'UNUSABLE')),
    metadata TEXT,  -- JSON
    
    captured_at TIMESTAMP NOT NULL,
    captured_by VARCHAR(100) NOT NULL,
    
    -- Audit Fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_biometric_prisoner FOREIGN KEY (prisoner_id) 
        REFERENCES prisoners(id) ON DELETE CASCADE
);

CREATE INDEX idx_biometric_prisoner ON biometric_data(prisoner_id);
CREATE INDEX idx_biometric_type ON biometric_data(biometric_type);
CREATE INDEX idx_biometric_captured ON biometric_data(captured_at);

-- ============================================
-- GHI CHÚ (Comments) - Mô tả bảng và cột
-- ============================================
-- Add comments for documentation
COMMENT ON TABLE prisoners IS 'Bảng hồ sơ tù nhân chính với thông tin nhân khẩu và trạng thái';
COMMENT ON TABLE biometric_data IS 'Dữ liệu sinh trắc học để nhận diện tù nhân';
COMMENT ON COLUMN prisoners.prisoner_number IS 'Mã số tù nhân duy nhất do hệ thống tạo';
COMMENT ON COLUMN prisoners.status IS 'Trạng thái hiện tại của tù nhân trong hệ thống';
COMMENT ON COLUMN biometric_data.template_data IS 'Dữ liệu sinh trắc học nhị phân tuân theo tiêu chuẩn FBI/NIST';
