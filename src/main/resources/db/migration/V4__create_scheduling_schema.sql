-- ============================================
-- V4: Scheduling Module Schema
-- Tạo schema quản lý lịch trình, ngày xét xử và cuộc hẹn
-- ============================================

-- ============================================
-- BẢNG: schedules
-- MỤC ĐÍCH: Quản lý trung tâm các lịch trình khác nhau của tù nhân
-- MÔ TẢ: Bảng quản lý lịch trình bao gồm:
--        - Loại lịch (tòa án, y tế, gặp khách, hoạt động, chuyển lao động, phóng thích, khác)
--        - Tiêu đề, mô tả chi tiết lịch trình
--        - Ngày/giờ bắt đầu - kết thúc, thời lượng, địa điểm
--        - Trạng thái (lên lịch, xác nhận, hoàn thành, hủy, sắp xếp lại, vắng mặt)
--        - Ưu tiên (thấp, bình thường, cao, khẩn cấp)
-- ============================================
-- Schedules Table (central scheduling)
CREATE TABLE schedules (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    prisoner_id VARCHAR(36),
    schedule_type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    scheduled_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP,
    duration_minutes INTEGER,
    location VARCHAR(255),
    status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED',
    priority VARCHAR(20) DEFAULT 'NORMAL',
    reminder_sent BOOLEAN DEFAULT FALSE,
    reminder_date TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_schedules_prisoner FOREIGN KEY (prisoner_id) REFERENCES prisoners(id) ON DELETE CASCADE,
    CONSTRAINT chk_schedule_type CHECK (schedule_type IN ('COURT', 'MEDICAL', 'VISITOR', 'ACTIVITY', 'TRANSFER', 'RELEASE', 'OTHER')),
    CONSTRAINT chk_schedule_status CHECK (status IN ('SCHEDULED', 'CONFIRMED', 'COMPLETED', 'CANCELLED', 'RESCHEDULED', 'NO_SHOW')),
    CONSTRAINT chk_schedule_priority CHECK (priority IN ('LOW', 'NORMAL', 'HIGH', 'URGENT')),
    CONSTRAINT chk_schedule_dates CHECK (end_date IS NULL OR end_date >= scheduled_date)
);

-- ============================================
-- BẢNG: court_dates
-- MỤC ĐÍCH: Quản lý thông tin lịch tòa và phiên tòa xét xử
-- MÔ TẢ: Lưu trữ chi tiết phiên tòa bao gồm:
--        - Tên tòa án, địa chỉ, số vụ án
--        - Loại phiên tòa (truy tố ban đầu, xét xử sơ bộ, xét xử chính thức, tuyên án, kháng cáo, tại ngoại, khác)
--        - Tên thẩm phán, luật sư, thông tin liên hệ
--        - Tội danh, kết quả xét xử, phán quyết
--        - Phán quyết (có tội, vô tội, chưa xác định, bị bác, thỏa thuận nhận tội)
--        - Lịch xét xử tiếp theo, sắp xếp chuyên chở
-- ============================================
-- Court Dates Table
CREATE TABLE court_dates (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    schedule_id UUID NOT NULL,
    prisoner_id VARCHAR(36) NOT NULL,
    court_name VARCHAR(255) NOT NULL,
    court_address TEXT,
    case_number VARCHAR(100),
    hearing_type VARCHAR(100) NOT NULL,
    judge_name VARCHAR(255),
    attorney_name VARCHAR(255),
    attorney_contact VARCHAR(255),
    charges TEXT,
    outcome TEXT,
    verdict VARCHAR(100),
    sentence TEXT,
    next_court_date DATE,
    transport_arranged BOOLEAN DEFAULT FALSE,
    transport_notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_court_dates_schedule FOREIGN KEY (schedule_id) REFERENCES schedules(id) ON DELETE CASCADE,
    CONSTRAINT fk_court_dates_prisoner FOREIGN KEY (prisoner_id) REFERENCES prisoners(id) ON DELETE CASCADE,
    CONSTRAINT chk_hearing_type CHECK (hearing_type IN ('ARRAIGNMENT', 'PRELIMINARY', 'TRIAL', 'SENTENCING', 'APPEAL', 'PAROLE', 'OTHER')),
    CONSTRAINT chk_verdict CHECK (verdict IN ('GUILTY', 'NOT_GUILTY', 'PENDING', 'DISMISSED', 'PLEA_BARGAIN'))
);

-- ============================================
-- BẢNG: appointments
-- MỤC ĐÍCH: Quản lý các cuộc hẹn với tù nhân (y tế, pháp lý, gặp khách, tư vấn...)
-- MÔ TẢ: Lưu trữ thông tin cuộc hẹn bao gồm:
--        - Loại cuộc hẹn (y tế, pháp lý, gặp gia đình, gặp lãnh đạo, tư vấn, giáo dục, khác)
--        - Mục đích cuộc hẹn, mô tả chi tiết
--        - Trạng thái (yêu cầu, phê duyệt, xác nhận, hoàn thành, hủy, từ chối, vắng mặt)
--        - Người yêu cầu, người phê duyệt, người xác nhận
--        - Trạng thái tham dự (có mặt, vắng mặt, trễ, rời sớm)
-- ============================================
-- Appointments Table
CREATE TABLE appointments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    schedule_id UUID NOT NULL,
    prisoner_id VARCHAR(36),
    visitor_id UUID,
    appointment_type VARCHAR(50) NOT NULL,
    purpose TEXT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'REQUESTED',
    requested_by VARCHAR(255) NOT NULL,
    approved_by VARCHAR(255),
    approved_at TIMESTAMP,
    confirmed_at TIMESTAMP,
    confirmed_by VARCHAR(255),
    cancelled_at TIMESTAMP,
    cancelled_by VARCHAR(255),
    cancellation_reason TEXT,
    attendance_status VARCHAR(50),
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_appointments_schedule FOREIGN KEY (schedule_id) REFERENCES schedules(id) ON DELETE CASCADE,
    CONSTRAINT fk_appointments_prisoner FOREIGN KEY (prisoner_id) REFERENCES prisoners(id) ON DELETE CASCADE,
    CONSTRAINT chk_appointment_type CHECK (appointment_type IN ('MEDICAL', 'LEGAL', 'FAMILY_VISIT', 'OFFICIAL_VISIT', 'COUNSELING', 'EDUCATION', 'OTHER')),
    CONSTRAINT chk_appointment_status CHECK (status IN ('REQUESTED', 'APPROVED', 'CONFIRMED', 'COMPLETED', 'CANCELLED', 'REJECTED', 'NO_SHOW')),
    CONSTRAINT chk_attendance_status CHECK (attendance_status IN ('PRESENT', 'ABSENT', 'LATE', 'EARLY_DEPARTURE'))
);

-- ============================================
-- CHỈ MỤC (Indexes) - Cải thiện hiệu suất truy vấn
-- ============================================
-- Indexes for performance
CREATE INDEX idx_schedules_prisoner_id ON schedules(prisoner_id);
CREATE INDEX idx_schedules_date ON schedules(scheduled_date DESC);
CREATE INDEX idx_schedules_type ON schedules(schedule_type);
CREATE INDEX idx_schedules_status ON schedules(status);
CREATE INDEX idx_schedules_priority ON schedules(priority);

CREATE INDEX idx_court_dates_schedule_id ON court_dates(schedule_id);
CREATE INDEX idx_court_dates_prisoner_id ON court_dates(prisoner_id);
CREATE INDEX idx_court_dates_case_number ON court_dates(case_number);
CREATE INDEX idx_court_dates_next_date ON court_dates(next_court_date);

CREATE INDEX idx_appointments_schedule_id ON appointments(schedule_id);
CREATE INDEX idx_appointments_prisoner_id ON appointments(prisoner_id);
CREATE INDEX idx_appointments_visitor_id ON appointments(visitor_id);
CREATE INDEX idx_appointments_type ON appointments(appointment_type);
CREATE INDEX idx_appointments_status ON appointments(status);

-- Comments
COMMENT ON TABLE schedules IS 'Central scheduling table for all types of prisoner schedules';
COMMENT ON TABLE court_dates IS 'Detailed court appearance information with legal details';
COMMENT ON TABLE appointments IS 'Appointments for medical, legal, visitor, and other purposes';
