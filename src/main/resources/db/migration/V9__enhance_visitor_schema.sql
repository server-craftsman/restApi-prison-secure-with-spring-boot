-- ============================================
-- V9: Visitor Module Enhancements
-- Tăng cường schema quản lý khách thăm
-- ============================================

-- ============================================
-- BẢNG: visitors
-- MỤC ĐÍCH: Lưu trữ thông tin của những người muốn thăm tù nhân
-- MÔ TẢ: Quản lý khách thăm bao gồm:
--        - Tên, ngày sinh, loại giấy tờ tùy thân (chứng minh thư, hộ chiếu, bằng lái xe, v.v.)
--        - Số giấy tờ tùy thân, số điện thoại, email, địa chỉ
--        - Ảnh khách thăm, trạng thái bị cấm, ghi chú
-- ============================================
-- Visitors Table
CREATE TABLE visitors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    identification_type VARCHAR(50) NOT NULL,
    identification_number VARCHAR(50) NOT NULL,
    contact_number VARCHAR(20),
    email VARCHAR(100),
    address TEXT,
    photo_url VARCHAR(255),
    is_banned BOOLEAN DEFAULT FALSE,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT uq_visitor_identification UNIQUE (identification_type, identification_number)
);

CREATE INDEX idx_visitors_name ON visitors(last_name, first_name);
CREATE INDEX idx_visitors_identification ON visitors(identification_number);

-- ============================================
-- BẢNG: visit_requests
-- MỤC ĐÍCH: Lưu trữ yêu cầu thăm viếng của khách
-- MÔ TẢ: Quản lý yêu cầu thăm bao gồm:
--        - Ngày/giờ mong muốn thăm, thời lượng, loại thăm (gia đình, pháp lý, chính thức, xã hội, tôn giáo, y tế, khác)
--        - Mục đích thăm, trạng thái (chờ xử lý, phê duyệt, từ chối, hủy, hết hạn)
--        - Người yêu cầu, người xem xét, ghi chú phê duyệt, lý do từ chối
-- ============================================
-- Visit Requests Table
CREATE TABLE visit_requests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    visitor_id UUID NOT NULL,
    prisoner_id VARCHAR(36) NOT NULL,
    requested_date TIMESTAMP NOT NULL,
    requested_duration INTEGER,
    visit_type VARCHAR(50) NOT NULL,
    visit_purpose TEXT,
    request_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    requested_by VARCHAR(255) NOT NULL,
    requested_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reviewed_by VARCHAR(255),
    reviewed_at TIMESTAMP,
    approval_notes TEXT,
    rejection_reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_visit_requests_visitor FOREIGN KEY (visitor_id) REFERENCES visitors(id) ON DELETE CASCADE,
    CONSTRAINT fk_visit_requests_prisoner FOREIGN KEY (prisoner_id) REFERENCES prisoners(id) ON DELETE CASCADE,
    CONSTRAINT chk_visit_type CHECK (visit_type IN ('FAMILY', 'LEGAL', 'OFFICIAL', 'SOCIAL', 'RELIGIOUS', 'MEDICAL', 'OTHER')),
    CONSTRAINT chk_request_status CHECK (request_status IN ('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED', 'EXPIRED'))
);

-- ============================================
-- BẢNG: visit_logs
-- MỤC ĐÍCH: Ghi nhật ký chi tiết mỗi lần thăm viếng
-- MÔ TẢ: Quản lý nhật ký thăm bao gồm:
--        - Thời gian nhận khách, kết thúc, thời gian thực tế
--        - Địa điểm thăm, nhân viên giám sát
--        - Ghi chú, có sự cố không, mô tả sự cố
--        - Hành vi khách thăm (xuất sắc, tốt, chấp nhận được, kém, bị cấm)
--        - Trạng thái thăm (lên lịch, đang diễn ra, hoàn thành, hủy, vắng mặt, bị chấm dứt)
--        - Vật dụng trao đổi
-- ============================================
-- Visit Logs Table
CREATE TABLE visit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    visit_request_id UUID,
    visitor_id UUID NOT NULL,
    prisoner_id VARCHAR(36) NOT NULL,
    visit_date TIMESTAMP NOT NULL,
    check_in_time TIMESTAMP,
    check_out_time TIMESTAMP,
    actual_duration INTEGER,
    visit_location VARCHAR(255),
    supervising_officer VARCHAR(255),
    visit_notes TEXT,
    incident_reported BOOLEAN DEFAULT FALSE,
    incident_details TEXT,
    visitor_behavior VARCHAR(50),
    items_exchanged TEXT,
    visit_status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_visit_logs_request FOREIGN KEY (visit_request_id) REFERENCES visit_requests(id) ON DELETE SET NULL,
    CONSTRAINT fk_visit_logs_visitor FOREIGN KEY (visitor_id) REFERENCES visitors(id) ON DELETE CASCADE,
    CONSTRAINT fk_visit_logs_prisoner FOREIGN KEY (prisoner_id) REFERENCES prisoners(id) ON DELETE CASCADE,
    CONSTRAINT chk_visitor_behavior CHECK (visitor_behavior IN ('EXCELLENT', 'GOOD', 'ACCEPTABLE', 'POOR', 'BANNED')),
    CONSTRAINT chk_visit_status CHECK (visit_status IN ('SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED', 'NO_SHOW', 'TERMINATED')),
    CONSTRAINT chk_visit_times CHECK (check_out_time IS NULL OR check_out_time >= check_in_time)
);

-- ============================================
-- BẢNG: visitation_rules
-- MỤC ĐÍCH: Lưu trữ các quy tắc thăm viếng của nhà tù
-- MÔ TẢ: Quản lý quy tắc thăm bao gồm:
--        - Tên quy tắc, mô tả chi tiết, loại quy tắc (chung, an ninh, y tế, đặc biệt, tạm thời)
--        - Áp dụng cho (tất cả, phân loại tù nhân, loại khách thăm, cụ thể)
--        - Phân loại tù nhân, loại khách thăm
--        - Số khách tối đa, thời lượng tối đa, ngày/giờ cho phép
--        - Danh sách vật cấm, yêu cầu đặc biệt
--        - Ngày có hiệu lực, ngày hết hạn
-- ============================================
-- Visitation Rules Table
CREATE TABLE visitation_rules (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    rule_name VARCHAR(255) NOT NULL UNIQUE,
    rule_description TEXT NOT NULL,
    rule_type VARCHAR(50) NOT NULL,
    applies_to VARCHAR(50) NOT NULL,
    prisoner_classification VARCHAR(50),
    visitor_type VARCHAR(50),
    max_visitors INTEGER,
    max_duration INTEGER,
    allowed_days VARCHAR(100),
    allowed_times VARCHAR(100),
    restricted_items TEXT,
    special_requirements TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    effective_date DATE NOT NULL,
    expiry_date DATE,
    created_by VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT chk_rule_type CHECK (rule_type IN ('GENERAL', 'SECURITY', 'HEALTH', 'SPECIAL', 'TEMPORARY')),
    CONSTRAINT chk_applies_to CHECK (applies_to IN ('ALL', 'PRISONER_CLASS', 'VISITOR_TYPE', 'SPECIFIC')),
    CONSTRAINT chk_rule_dates CHECK (expiry_date IS NULL OR expiry_date >= effective_date)
);

-- ============================================
-- BẢNG: visitor_restrictions
-- MỤC ĐÍCH: Lưu trữ các hạn chế thăm viếng áp dụng cho khách thăm cụ thể
-- MÔ TẢ: Quản lý hạn chế bao gồm:
--        - Loại hạn chế (bị cấm, chỉ giám sát, thời gian hạn chế, tù nhân cụ thể, tạm hoãn)
--        - Lý do hạn chế, ngày bắt đầu - kết thúc
--        - Vĩnh viễn hay tạm thời, người áp dụng, ngày xem xét lại
--        - Trạng thái (hoạt động, hết hạn, bị hủy, đang xem xét), ghi chú
-- ============================================
-- Visitor Restrictions Table
CREATE TABLE visitor_restrictions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    visitor_id UUID NOT NULL,
    prisoner_id VARCHAR(36),
    restriction_type VARCHAR(50) NOT NULL,
    restriction_reason TEXT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    is_permanent BOOLEAN DEFAULT FALSE,
    imposed_by VARCHAR(255) NOT NULL,
    review_date DATE,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_visitor_restrictions_visitor FOREIGN KEY (visitor_id) REFERENCES visitors(id) ON DELETE CASCADE,
    CONSTRAINT fk_visitor_restrictions_prisoner FOREIGN KEY (prisoner_id) REFERENCES prisoners(id) ON DELETE CASCADE,
    CONSTRAINT chk_restriction_type CHECK (restriction_type IN ('BANNED', 'SUPERVISED_ONLY', 'LIMITED_TIME', 'SPECIFIC_PRISONER', 'TEMPORARY_SUSPENSION')),
    CONSTRAINT chk_restriction_status CHECK (status IN ('ACTIVE', 'EXPIRED', 'REVOKED', 'UNDER_REVIEW')),
    CONSTRAINT chk_restriction_dates CHECK (end_date IS NULL OR end_date >= start_date)
);

-- ============================================
-- CHỈ MỤC (Indexes) - Cải thiện hiệu suất truy vấn
-- ============================================
-- Indexes
CREATE INDEX idx_visit_requests_visitor ON visit_requests(visitor_id);
CREATE INDEX idx_visit_requests_prisoner ON visit_requests(prisoner_id);
CREATE INDEX idx_visit_requests_date ON visit_requests(requested_date DESC);
CREATE INDEX idx_visit_requests_status ON visit_requests(request_status);

CREATE INDEX idx_visit_logs_request ON visit_logs(visit_request_id);
CREATE INDEX idx_visit_logs_visitor ON visit_logs(visitor_id);
CREATE INDEX idx_visit_logs_prisoner ON visit_logs(prisoner_id);
CREATE INDEX idx_visit_logs_date ON visit_logs(visit_date DESC);
CREATE INDEX idx_visit_logs_status ON visit_logs(visit_status);

CREATE INDEX idx_visitation_rules_type ON visitation_rules(rule_type);
CREATE INDEX idx_visitation_rules_active ON visitation_rules(is_active);
CREATE INDEX idx_visitation_rules_effective ON visitation_rules(effective_date);

CREATE INDEX idx_visitor_restrictions_visitor ON visitor_restrictions(visitor_id);
CREATE INDEX idx_visitor_restrictions_prisoner ON visitor_restrictions(prisoner_id);
CREATE INDEX idx_visitor_restrictions_status ON visitor_restrictions(status);
CREATE INDEX idx_visitor_restrictions_dates ON visitor_restrictions(start_date, end_date);

-- Comments
COMMENT ON TABLE visit_requests IS 'Visit requests submitted by or for visitors';
COMMENT ON TABLE visit_logs IS 'Detailed logs of all visits including check-in/out times';
COMMENT ON TABLE visitation_rules IS 'Rules and policies governing prison visitation';
COMMENT ON TABLE visitor_restrictions IS 'Restrictions placed on specific visitors';
