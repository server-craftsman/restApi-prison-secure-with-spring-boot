-- ============================================
-- V5: Workflow Control Module Schema
-- Tạo schema quản lý quy trình công việc trong nhà tù
-- ============================================

-- ============================================
-- BẢNG: workflows
-- MỤC ĐÍCH: Lưu trữ định nghĩa các quy trình công việc (workflow)
-- MÔ TẢ: Quản lý các loại quy trình bao gồm:
--        - Loại quy trình (nhập viện, phóng thích, chuyển lao động, phân loại, kỷ luật, cấp cứu y tế, tại ngoại, khác)
--        - Tên quy trình, mô tả chi tiết
--        - Trạng thái hoạt động (có/không)
--        - Mỗi loại workflow là duy nhất
-- ============================================
-- Workflows Table (workflow definitions)
CREATE TABLE workflows (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    workflow_type VARCHAR(50) NOT NULL UNIQUE,
    workflow_name VARCHAR(255) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT chk_workflow_type CHECK (workflow_type IN ('ADMISSION', 'RELEASE', 'TRANSFER', 'CLASSIFICATION', 'DISCIPLINARY', 'MEDICAL_EMERGENCY', 'PAROLE', 'OTHER'))
);

-- ============================================
-- BẢNG: workflow_instances
-- MỤC ĐÍCH: Lưu trữ các phiên bản cụ thể của quy trình công việc đang thực hiện
-- MÔ TẢ: Quản lý thực thi quy trình bao gồm:
--        - Quy trình áp dụng (tham chiếu đến workflows)
--        - Tù nhân/Đơn nhập viện liên quan
--        - Trạng thái (bắt đầu, đang thực hiện, chờ phê duyệt, phê duyệt, từ chối, hoàn thành, hủy, tạm dừng)
--        - Bước hiện tại, tổng số bước
--        - Ưu tiên (thấp, bình thường, cao, khẩn cấp)
--        - Thời gian bắt đầu - kết thúc, người phân công, metadata JSON
-- ============================================
-- Workflow Instances Table
CREATE TABLE workflow_instances (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    workflow_id UUID NOT NULL,
    prisoner_id VARCHAR(36),
    booking_id VARCHAR(36),
    reference_id UUID,
    reference_type VARCHAR(50),
    status VARCHAR(50) NOT NULL DEFAULT 'INITIATED',
    current_step INTEGER NOT NULL DEFAULT 1,
    total_steps INTEGER NOT NULL,
    priority VARCHAR(20) DEFAULT 'NORMAL',
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    initiated_by VARCHAR(255) NOT NULL,
    assigned_to VARCHAR(255),
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_workflow_instances_workflow FOREIGN KEY (workflow_id) REFERENCES workflows(id) ON DELETE RESTRICT,
    CONSTRAINT fk_workflow_instances_prisoner FOREIGN KEY (prisoner_id) REFERENCES prisoners(id) ON DELETE CASCADE,
    CONSTRAINT fk_workflow_instances_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    CONSTRAINT chk_instance_status CHECK (status IN ('INITIATED', 'IN_PROGRESS', 'AWAITING_APPROVAL', 'APPROVED', 'REJECTED', 'COMPLETED', 'CANCELLED', 'ON_HOLD')),
    CONSTRAINT chk_instance_priority CHECK (priority IN ('LOW', 'NORMAL', 'HIGH', 'URGENT')),
    CONSTRAINT chk_reference_type CHECK (reference_type IN ('PRISONER', 'BOOKING', 'MEDICAL', 'VISITOR', 'SCHEDULE', 'OTHER'))
);

-- ============================================
-- BẢNG: workflow_steps
-- MỤC ĐÍCH: Lưu trữ các bước chi tiết trong một phiên bản quy trình công việc
-- MÔ TẢ: Quản lý từng bước của quy trình bao gồm:
--        - Số bước, tên bước, mô tả chi tiết
--        - Người phân công, vai trò phân công
--        - Trạng thái bước (chưa thực hiện, đang thực hiện, hoàn thành, bỏ qua, thất bại, chờ phê duyệt)
--        - Thời gian bắt đầu - kết thúc, người hoàn thành
--        - Hạn chót thực hiện
--        - Cần phê duyệt, bước blocking (bắt buộc phải hoàn thành)
-- ============================================
-- Workflow Steps Table
CREATE TABLE workflow_steps (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    workflow_instance_id UUID NOT NULL,
    step_number INTEGER NOT NULL,
    step_name VARCHAR(255) NOT NULL,
    step_description TEXT,
    assigned_to VARCHAR(255),
    assigned_role VARCHAR(100),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    completed_by VARCHAR(255),
    due_date TIMESTAMP,
    is_approval_required BOOLEAN DEFAULT FALSE,
    is_blocking BOOLEAN DEFAULT TRUE,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_workflow_steps_instance FOREIGN KEY (workflow_instance_id) REFERENCES workflow_instances(id) ON DELETE CASCADE,
    CONSTRAINT chk_step_status CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'SKIPPED', 'FAILED', 'AWAITING_APPROVAL')),
    CONSTRAINT uq_workflow_step UNIQUE (workflow_instance_id, step_number)
);

-- ============================================
-- BẢNG: approvals
-- MỤC ĐÍCH: Lưu trữ quá trình phê duyệt từng bước trong quy trình công việc
-- MÔ TẢ: Quản lý phê duyệt bao gồm:
--        - Người phê duyệt, vai trò phê duyệt
--        - Trạng thái phê duyệt (chờ, phê duyệt, từ chối, chuyển giao, hết hạn)
--        - Thời gian phê duyệt/từ chối
--        - Nhận xét/lý do phê duyệt hoặc từ chối
--        - Cấp độ phê duyệt, phê duyệt cuối cùng hay không
-- ============================================
-- Approvals Table
CREATE TABLE approvals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    workflow_step_id UUID NOT NULL,
    workflow_instance_id UUID NOT NULL,
    approver_name VARCHAR(255) NOT NULL,
    approver_role VARCHAR(100),
    approval_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    approved_at TIMESTAMP,
    rejected_at TIMESTAMP,
    comments TEXT,
    approval_level INTEGER DEFAULT 1,
    is_final_approval BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_approvals_step FOREIGN KEY (workflow_step_id) REFERENCES workflow_steps(id) ON DELETE CASCADE,
    CONSTRAINT fk_approvals_instance FOREIGN KEY (workflow_instance_id) REFERENCES workflow_instances(id) ON DELETE CASCADE,
    CONSTRAINT chk_approval_status CHECK (approval_status IN ('PENDING', 'APPROVED', 'REJECTED', 'DELEGATED', 'EXPIRED'))
);

-- Indexes
CREATE INDEX idx_workflows_type ON workflows(workflow_type);
CREATE INDEX idx_workflows_active ON workflows(is_active);

CREATE INDEX idx_workflow_instances_workflow ON workflow_instances(workflow_id);
CREATE INDEX idx_workflow_instances_prisoner ON workflow_instances(prisoner_id);
CREATE INDEX idx_workflow_instances_booking ON workflow_instances(booking_id);
CREATE INDEX idx_workflow_instances_status ON workflow_instances(status);
CREATE INDEX idx_workflow_instances_started ON workflow_instances(started_at DESC);

CREATE INDEX idx_workflow_steps_instance ON workflow_steps(workflow_instance_id);
CREATE INDEX idx_workflow_steps_status ON workflow_steps(status);
CREATE INDEX idx_workflow_steps_assigned ON workflow_steps(assigned_to);

CREATE INDEX idx_approvals_step ON approvals(workflow_step_id);
CREATE INDEX idx_approvals_instance ON approvals(workflow_instance_id);
CREATE INDEX idx_approvals_approver ON approvals(approver_name);
CREATE INDEX idx_approvals_status ON approvals(approval_status);

-- Comments
COMMENT ON TABLE workflows IS 'Workflow definitions for various prison processes';
COMMENT ON TABLE workflow_instances IS 'Active workflow instances tracking process execution';
COMMENT ON TABLE workflow_steps IS 'Individual steps within workflow instances';
COMMENT ON TABLE approvals IS 'Approval records for workflow steps requiring authorization';
