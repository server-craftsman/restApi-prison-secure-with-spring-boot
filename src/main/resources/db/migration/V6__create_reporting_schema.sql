-- ============================================
-- V6: Reporting Module Schema
-- Tạo schema quản lý báo cáo và mẫu báo cáo
-- ============================================

-- ============================================
-- BẢNG: reports
-- MỤC ĐÍCH: Lưu trữ thông tin báo cáo đã tạo
-- MÔ TẢ: Quản lý báo cáo bao gồm:
--        - Loại báo cáo (sức chứa, thống kê tù nhân, y tế, nhật ký khách thăm, tóm tắt nhập viện, xác minh sinh trắc học, sự cố, tùy chỉnh)
--        - Người tạo, thời gian tạo, khoảng thời gian báo cáo
--        - Tham số báo cáo (JSON), đường dẫn file, định dạng (PDF, Excel, CSV, JSON, HTML)
--        - Kích thước file, trạng thái (tạo, thất bại, hết hạn, lưu trữ)
--        - Ngày hết hạn
-- ============================================
-- Reports Table
CREATE TABLE reports (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    report_type VARCHAR(100) NOT NULL,
    report_name VARCHAR(255) NOT NULL,
    description TEXT,
    generated_by VARCHAR(255) NOT NULL,
    generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    start_date DATE,
    end_date DATE,
    parameters JSONB,
    file_path VARCHAR(500),
    file_format VARCHAR(20),
    file_size BIGINT,
    status VARCHAR(50) NOT NULL DEFAULT 'GENERATED',
    error_message TEXT,
    expires_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT chk_report_type CHECK (report_type IN ('OCCUPANCY', 'PRISONER_STATISTICS', 'MEDICAL_SUMMARY', 'VISITOR_LOG', 'BOOKING_SUMMARY', 'BIOMETRIC_VERIFICATION', 'INCIDENT', 'CUSTOM')),
    CONSTRAINT chk_report_format CHECK (file_format IN ('PDF', 'EXCEL', 'CSV', 'JSON', 'HTML')),
    CONSTRAINT chk_report_status CHECK (status IN ('GENERATED', 'FAILED', 'EXPIRED', 'ARCHIVED')),
    CONSTRAINT chk_report_dates CHECK (end_date IS NULL OR end_date >= start_date)
);

-- ============================================
-- BẢNG: report_templates
-- MỤC ĐÍCH: Lưu trữ các mẫu báo cáo có thể tái sử dụng
-- MÔ TẢ: Quản lý mẫu báo cáo bao gồm:
--        - Tên mẫu (duy nhất), loại mẫu, mô tả
--        - Truy vấn SQL, schema tham số (JSON)
--        - Định dạng đầu ra (PDF, Excel, CSV, JSON, HTML)
--        - Trạng thái hoạt động, lên lịch định kỳ
--        - Biểu thức Cron cho lịch trình tự động
--        - Người tạo, thời gian cập nhật
-- ============================================
-- Report Templates Table
CREATE TABLE report_templates (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    template_name VARCHAR(255) NOT NULL UNIQUE,
    template_type VARCHAR(100) NOT NULL,
    description TEXT,
    sql_query TEXT,
    parameters_schema JSONB,
    output_format VARCHAR(20) DEFAULT 'PDF',
    is_active BOOLEAN DEFAULT TRUE,
    is_scheduled BOOLEAN DEFAULT FALSE,
    schedule_cron VARCHAR(100),
    created_by VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT chk_template_type CHECK (template_type IN ('OCCUPANCY', 'STATISTICS', 'MEDICAL', 'VISITOR', 'CUSTOM')),
    CONSTRAINT chk_template_format CHECK (output_format IN ('PDF', 'EXCEL', 'CSV', 'JSON', 'HTML'))
);

-- ============================================
-- BẢNG: report_schedules
-- MỤC ĐÍCH: Quản lý lịch tạo báo cáo tự động
-- MÔ TẢ: Quản lý lịch trình báo cáo bao gồm:
--        - Tên lịch trình, biểu thức Cron (định kỳ)
--        - Trạng thái hoạt động
--        - Thời gian chạy lần cuối, lần tiếp theo
--        - Tham số báo cáo, danh sách người nhận email
--        - Người tạo, thời gian cập nhật
-- ============================================
-- Report Schedules Table
CREATE TABLE report_schedules (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    template_id UUID NOT NULL,
    schedule_name VARCHAR(255) NOT NULL,
    cron_expression VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    last_run_at TIMESTAMP,
    next_run_at TIMESTAMP,
    parameters JSONB,
    recipients TEXT,
    created_by VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_report_schedules_template FOREIGN KEY (template_id) REFERENCES report_templates(id) ON DELETE CASCADE
);

-- ============================================
-- CHỈ MỤC (Indexes) - Cải thiện hiệu suất truy vấn
-- ============================================
-- Indexes
CREATE INDEX idx_reports_type ON reports(report_type);
CREATE INDEX idx_reports_generated_date ON reports(generated_at DESC);
CREATE INDEX idx_reports_generated_by ON reports(generated_by);
CREATE INDEX idx_reports_status ON reports(status);
CREATE INDEX idx_reports_dates ON reports(start_date, end_date);

CREATE INDEX idx_report_templates_type ON report_templates(template_type);
CREATE INDEX idx_report_templates_active ON report_templates(is_active);
CREATE INDEX idx_report_templates_scheduled ON report_templates(is_scheduled);

CREATE INDEX idx_report_schedules_template ON report_schedules(template_id);
CREATE INDEX idx_report_schedules_active ON report_schedules(is_active);
CREATE INDEX idx_report_schedules_next_run ON report_schedules(next_run_at);

-- Comments
COMMENT ON TABLE reports IS 'Generated reports with metadata and file references';
COMMENT ON TABLE report_templates IS 'Reusable report templates with SQL queries and parameters';
COMMENT ON TABLE report_schedules IS 'Scheduled report generation configurations';
