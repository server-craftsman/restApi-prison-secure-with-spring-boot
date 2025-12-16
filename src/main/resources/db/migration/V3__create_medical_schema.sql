-- ============================================
-- V3: Medical Management Module Schema
-- Tạo schema quản lý hồ sơ y tế của tù nhân
-- ============================================

-- ============================================
-- BẢNG: medical_records
-- MỤC ĐÍCH: Lưu trữ hồ sơ khám bệnh và điều trị của tù nhân
-- MÔ TẢ: Quản lý thông tin y tế bao gồm:
--        - Loại khám (tổng quát, cấp cứu, khám định kỳ, chuyên khoa, sức khỏe tâm thần, nha khoa)
--        - Chẩn đoán, điều trị, ghi chú từ nhân viên y tế
--        - Mức độ nghiêm trọng (thấp, trung bình, cao, nguy kịch)
--        - Theo dõi theo dõi tiếp theo nếu cần
-- ============================================
CREATE TABLE medical_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    prisoner_id VARCHAR(36) NOT NULL,
    record_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    record_type VARCHAR(50) NOT NULL,
    diagnosis TEXT,
    treatment TEXT,
    medical_staff VARCHAR(255) NOT NULL,
    notes TEXT,
    severity VARCHAR(20),
    follow_up_required BOOLEAN DEFAULT FALSE,
    follow_up_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_medical_records_prisoner FOREIGN KEY (prisoner_id) REFERENCES prisoners(id) ON DELETE CASCADE,
    CONSTRAINT chk_record_type CHECK (record_type IN ('GENERAL', 'EMERGENCY', 'ROUTINE_CHECKUP', 'SPECIALIST', 'MENTAL_HEALTH', 'DENTAL', 'OTHER')),
    CONSTRAINT chk_severity CHECK (severity IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL'))
);

-- ============================================
-- BẢNG: prescriptions
-- MỤC ĐÍCH: Lưu trữ đơn thuốc và thông tin điều trị bằng thuốc
-- MÔ TẢ: Quản lý các đơn thuốc bao gồm:
--        - Tên thuốc, liều lượng, tần suất sử dụng
--        - Đường dùng (qua đường miệng, tiêm, bôi, tiêm tĩnh mạch)
--        - Ngày bắt đầu - kết thúc điều trị
--        - Trạng thái đơn (hoạt động, hoàn thành, dừng, tạm dừng)
--        - Lý do dừng thuốc nếu có
-- ============================================
-- Prescriptions Table
CREATE TABLE prescriptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    medical_record_id UUID,
    prisoner_id VARCHAR(36) NOT NULL,
    medication_name VARCHAR(255) NOT NULL,
    dosage VARCHAR(100) NOT NULL,
    frequency VARCHAR(100) NOT NULL,
    route VARCHAR(50),
    start_date DATE NOT NULL,
    end_date DATE,
    prescribing_doctor VARCHAR(255) NOT NULL,
    pharmacy_notes TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    discontinue_reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_prescriptions_medical_record FOREIGN KEY (medical_record_id) REFERENCES medical_records(id) ON DELETE SET NULL,
    CONSTRAINT fk_prescriptions_prisoner FOREIGN KEY (prisoner_id) REFERENCES prisoners(id) ON DELETE CASCADE,
    CONSTRAINT chk_prescription_status CHECK (status IN ('ACTIVE', 'COMPLETED', 'DISCONTINUED', 'ON_HOLD')),
    CONSTRAINT chk_prescription_route CHECK (route IN ('ORAL', 'INJECTION', 'TOPICAL', 'INTRAVENOUS', 'OTHER')),
    CONSTRAINT chk_prescription_dates CHECK (end_date IS NULL OR end_date >= start_date)
);

-- ============================================
-- BẢNG: health_checkups
-- MỤC ĐÍCH: Lưu trữ kết quả khám sức khỏe định kỳ
-- MÔ TẢ: Quản lý thông tin khám sức khỏe bao gồm:
--        - Loại khám (nhập viện, định kỳ, theo dõi, cấp cứu, trước phóng thích, hàng năm)
--        - Các chỉ số sức khỏe: huyết áp, nhịp tim, thân nhiệt, cân nặng, chiều cao, BMI
--        - Đường huyết, độ bão hòa oxy, tần suất thở, tình trạng tổng quát
--        - Tình trạng sức khỏe (xuất sắc, tốt, vừa phải, kém, nguy kịch)
--        - Lịch khám tiếp theo
-- ============================================
-- Health Checkups Table
CREATE TABLE health_checkups (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    prisoner_id VARCHAR(36) NOT NULL,
    checkup_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    checkup_type VARCHAR(100) NOT NULL,
    blood_pressure VARCHAR(20),
    pulse_rate INTEGER,
    temperature DECIMAL(4,2),
    weight DECIMAL(5,2),
    height DECIMAL(5,2),
    bmi DECIMAL(4,2),
    blood_sugar DECIMAL(5,2),
    oxygen_saturation INTEGER,
    respiratory_rate INTEGER,
    general_condition VARCHAR(50),
    notes TEXT,
    next_checkup_date DATE,
    performed_by VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_health_checkups_prisoner FOREIGN KEY (prisoner_id) REFERENCES prisoners(id) ON DELETE CASCADE,
    CONSTRAINT chk_checkup_type CHECK (checkup_type IN ('ADMISSION', 'ROUTINE', 'FOLLOW_UP', 'EMERGENCY', 'PRE_RELEASE', 'ANNUAL')),
    CONSTRAINT chk_general_condition CHECK (general_condition IN ('EXCELLENT', 'GOOD', 'FAIR', 'POOR', 'CRITICAL')),
    CONSTRAINT chk_vital_signs CHECK (
        (pulse_rate IS NULL OR (pulse_rate >= 40 AND pulse_rate <= 200)) AND
        (temperature IS NULL OR (temperature >= 35.0 AND temperature <= 42.0)) AND
        (weight IS NULL OR (weight >= 30.0 AND weight <= 300.0)) AND
        (height IS NULL OR (height >= 100.0 AND height <= 250.0)) AND
        (oxygen_saturation IS NULL OR (oxygen_saturation >= 0 AND oxygen_saturation <= 100))
    )
);

-- ============================================
-- CHỈ MỤC (Indexes) - Cải thiện hiệu suất truy vấn
-- ============================================
-- Indexes for performance
CREATE INDEX idx_medical_records_prisoner_id ON medical_records(prisoner_id);
CREATE INDEX idx_medical_records_record_date ON medical_records(record_date DESC);
CREATE INDEX idx_medical_records_type ON medical_records(record_type);
CREATE INDEX idx_medical_records_severity ON medical_records(severity);

CREATE INDEX idx_prescriptions_prisoner_id ON prescriptions(prisoner_id);
CREATE INDEX idx_prescriptions_medical_record_id ON prescriptions(medical_record_id);
CREATE INDEX idx_prescriptions_status ON prescriptions(status);
CREATE INDEX idx_prescriptions_start_date ON prescriptions(start_date DESC);
CREATE INDEX idx_prescriptions_medication ON prescriptions(medication_name);

CREATE INDEX idx_health_checkups_prisoner_id ON health_checkups(prisoner_id);
CREATE INDEX idx_health_checkups_date ON health_checkups(checkup_date DESC);
CREATE INDEX idx_health_checkups_type ON health_checkups(checkup_type);
CREATE INDEX idx_health_checkups_next_checkup_date ON health_checkups(next_checkup_date);

-- Comments for documentation
COMMENT ON TABLE medical_records IS 'Stores medical records for prisoners including diagnoses and treatments';
COMMENT ON TABLE prescriptions IS 'Tracks medication prescriptions for prisoners';
COMMENT ON TABLE health_checkups IS 'Records routine and emergency health checkups with vital signs';

COMMENT ON COLUMN medical_records.severity IS 'Severity level of the medical condition';
COMMENT ON COLUMN prescriptions.route IS 'Administration route of the medication';
COMMENT ON COLUMN health_checkups.bmi IS 'Body Mass Index calculated from weight and height';
