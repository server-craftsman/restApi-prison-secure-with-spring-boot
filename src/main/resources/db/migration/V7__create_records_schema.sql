-- Records Management Module Schema
-- Version: V7
-- Description: Creates tables for document management and access control

-- Document Categories Table
CREATE TABLE document_categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    category_name VARCHAR(255) NOT NULL,
    description TEXT,
    parent_category_id UUID,
    category_path VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_document_categories_parent FOREIGN KEY (parent_category_id) REFERENCES document_categories(id) ON DELETE SET NULL,
    CONSTRAINT uq_category_name UNIQUE (category_name, parent_category_id)
);

-- Documents Table
CREATE TABLE documents (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    prisoner_id VARCHAR(36),
    booking_id VARCHAR(36),
    category_id UUID,
    document_type VARCHAR(100) NOT NULL,
    document_name VARCHAR(255) NOT NULL,
    description TEXT,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    checksum VARCHAR(64),
    version_number INTEGER DEFAULT 1,
    is_current_version BOOLEAN DEFAULT TRUE,
    parent_document_id UUID,
    uploaded_by VARCHAR(255) NOT NULL,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expiry_date DATE,
    is_confidential BOOLEAN DEFAULT FALSE,
    is_archived BOOLEAN DEFAULT FALSE,
    archived_at TIMESTAMP,
    tags TEXT[],
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_documents_prisoner FOREIGN KEY (prisoner_id) REFERENCES prisoners(id) ON DELETE CASCADE,
    CONSTRAINT fk_documents_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    CONSTRAINT fk_documents_category FOREIGN KEY (category_id) REFERENCES document_categories(id) ON DELETE SET NULL,
    CONSTRAINT fk_documents_parent FOREIGN KEY (parent_document_id) REFERENCES documents(id) ON DELETE SET NULL,
    CONSTRAINT chk_document_type CHECK (document_type IN ('LEGAL', 'MEDICAL', 'IDENTIFICATION', 'COURT_ORDER', 'TRANSFER', 'PROPERTY', 'COMPLAINT', 'INCIDENT', 'PHOTO', 'OTHER'))
);

-- Document Access Log Table
CREATE TABLE document_access_log (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document_id UUID NOT NULL,
    accessed_by VARCHAR(255) NOT NULL,
    access_type VARCHAR(50) NOT NULL,
    accessed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(50),
    user_agent VARCHAR(500),
    success BOOLEAN DEFAULT TRUE,
    failure_reason TEXT,
    
    CONSTRAINT fk_document_access_log_document FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE,
    CONSTRAINT chk_access_type CHECK (access_type IN ('VIEW', 'DOWNLOAD', 'PRINT', 'EDIT', 'DELETE', 'SHARE'))
);

-- Document Permissions Table
CREATE TABLE document_permissions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document_id UUID NOT NULL,
    user_or_role VARCHAR(255) NOT NULL,
    permission_type VARCHAR(20) NOT NULL,
    is_role BOOLEAN DEFAULT FALSE,
    granted_by VARCHAR(255),
    granted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_document_permissions_document FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE,
    CONSTRAINT chk_permission_type CHECK (permission_type IN ('READ', 'WRITE', 'DELETE', 'SHARE', 'ADMIN')),
    CONSTRAINT uq_document_permission UNIQUE (document_id, user_or_role, permission_type)
);

-- Indexes
CREATE INDEX idx_document_categories_parent ON document_categories(parent_category_id);
CREATE INDEX idx_document_categories_active ON document_categories(is_active);

CREATE INDEX idx_documents_prisoner ON documents(prisoner_id);
CREATE INDEX idx_documents_booking ON documents(booking_id);
CREATE INDEX idx_documents_category ON documents(category_id);
CREATE INDEX idx_documents_type ON documents(document_type);
CREATE INDEX idx_documents_uploaded_date ON documents(uploaded_at DESC);
CREATE INDEX idx_documents_uploaded_by ON documents(uploaded_by);
CREATE INDEX idx_documents_current_version ON documents(is_current_version);
CREATE INDEX idx_documents_archived ON documents(is_archived);
CREATE INDEX idx_documents_parent ON documents(parent_document_id);
CREATE INDEX idx_documents_tags ON documents USING GIN(tags);

CREATE INDEX idx_document_access_log_document ON document_access_log(document_id);
CREATE INDEX idx_document_access_log_user ON document_access_log(accessed_by);
CREATE INDEX idx_document_access_log_date ON document_access_log(accessed_at DESC);
CREATE INDEX idx_document_access_log_type ON document_access_log(access_type);

CREATE INDEX idx_document_permissions_document ON document_permissions(document_id);
CREATE INDEX idx_document_permissions_user ON document_permissions(user_or_role);

-- Comments
COMMENT ON TABLE document_categories IS 'Hierarchical categories for organizing documents';
COMMENT ON TABLE documents IS 'Stores document metadata and file references with versioning';
COMMENT ON TABLE document_access_log IS 'Audit log for all document access activities';
COMMENT ON TABLE document_permissions IS 'Access control permissions for documents';
