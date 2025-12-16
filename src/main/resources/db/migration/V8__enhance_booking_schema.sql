-- Booking Module Enhancements
-- Version: V8
-- Description: Adds tables for booking charges, property items, and cell assignments

-- Booking Charges Table
CREATE TABLE booking_charges (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_id VARCHAR(36) NOT NULL,
    charge_type VARCHAR(100) NOT NULL,
    charge_description TEXT NOT NULL,
    statute_code VARCHAR(100),
    charge_level VARCHAR(50),
    bail_amount DECIMAL(12,2),
    bond_type VARCHAR(50),
    court_case_number VARCHAR(100),
    charge_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    filed_date DATE,
    disposition VARCHAR(100),
    disposition_date DATE,
    sentence_length VARCHAR(100),
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_booking_charges_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    CONSTRAINT chk_charge_type CHECK (charge_type IN ('FELONY', 'MISDEMEANOR', 'INFRACTION', 'VIOLATION', 'OTHER')),
    CONSTRAINT chk_charge_level CHECK (charge_level IN ('FIRST_DEGREE', 'SECOND_DEGREE', 'THIRD_DEGREE', 'CLASS_A', 'CLASS_B', 'CLASS_C', 'OTHER')),
    CONSTRAINT chk_bond_type CHECK (bond_type IN ('CASH', 'SURETY', 'PROPERTY', 'PERSONAL_RECOGNIZANCE', 'NO_BAIL')),
    CONSTRAINT chk_charge_status CHECK (charge_status IN ('PENDING', 'FILED', 'DISMISSED', 'CONVICTED', 'ACQUITTED', 'PLEA_BARGAIN'))
);

-- Property Items Table
CREATE TABLE property_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_id VARCHAR(36) NOT NULL,
    prisoner_id VARCHAR(36) NOT NULL,
    item_number VARCHAR(50) UNIQUE NOT NULL,
    item_category VARCHAR(100) NOT NULL,
    item_description TEXT NOT NULL,
    quantity INTEGER DEFAULT 1,
    estimated_value DECIMAL(10,2),
    condition VARCHAR(50),
    storage_location VARCHAR(255),
    is_contraband BOOLEAN DEFAULT FALSE,
    disposition VARCHAR(50) DEFAULT 'STORED',
    received_by VARCHAR(255) NOT NULL,
    received_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    returned_to VARCHAR(255),
    returned_at TIMESTAMP,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_property_items_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    CONSTRAINT fk_property_items_prisoner FOREIGN KEY (prisoner_id) REFERENCES prisoners(id) ON DELETE CASCADE,
    CONSTRAINT chk_item_category CHECK (item_category IN ('CLOTHING', 'JEWELRY', 'ELECTRONICS', 'DOCUMENTS', 'CASH', 'MEDICATION', 'PERSONAL_ITEMS', 'OTHER')),
    CONSTRAINT chk_item_condition CHECK (condition IN ('EXCELLENT', 'GOOD', 'FAIR', 'POOR', 'DAMAGED')),
    CONSTRAINT chk_item_disposition CHECK (disposition IN ('STORED', 'RETURNED', 'DESTROYED', 'TRANSFERRED', 'EVIDENCE', 'DONATED'))
);

-- Cell Assignments Table
CREATE TABLE cell_assignments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    prisoner_id VARCHAR(36) NOT NULL,
    booking_id VARCHAR(36) NOT NULL,
    cell_number VARCHAR(50) NOT NULL,
    cell_block VARCHAR(50) NOT NULL,
    cell_type VARCHAR(50),
    bed_number VARCHAR(20),
    assignment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    release_date TIMESTAMP,
    assignment_status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    assignment_reason VARCHAR(255),
    assigned_by VARCHAR(255) NOT NULL,
    released_by VARCHAR(255),
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_cell_assignments_prisoner FOREIGN KEY (prisoner_id) REFERENCES prisoners(id) ON DELETE CASCADE,
    CONSTRAINT fk_cell_assignments_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    CONSTRAINT chk_cell_type CHECK (cell_type IN ('GENERAL', 'ISOLATION', 'MEDICAL', 'PROTECTIVE_CUSTODY', 'DISCIPLINARY', 'INTAKE')),
    CONSTRAINT chk_assignment_status CHECK (assignment_status IN ('ACTIVE', 'COMPLETED', 'TRANSFERRED', 'RELEASED')),
    CONSTRAINT chk_assignment_dates CHECK (release_date IS NULL OR release_date >= assignment_date)
);

-- Indexes
CREATE INDEX idx_booking_charges_booking ON booking_charges(booking_id);
CREATE INDEX idx_booking_charges_status ON booking_charges(charge_status);
CREATE INDEX idx_booking_charges_case_number ON booking_charges(court_case_number);
CREATE INDEX idx_booking_charges_filed_date ON booking_charges(filed_date DESC);

CREATE INDEX idx_property_items_booking ON property_items(booking_id);
CREATE INDEX idx_property_items_prisoner ON property_items(prisoner_id);
CREATE INDEX idx_property_items_number ON property_items(item_number);
CREATE INDEX idx_property_items_category ON property_items(item_category);
CREATE INDEX idx_property_items_disposition ON property_items(disposition);

CREATE INDEX idx_cell_assignments_prisoner ON cell_assignments(prisoner_id);
CREATE INDEX idx_cell_assignments_booking ON cell_assignments(booking_id);
CREATE INDEX idx_cell_assignments_cell ON cell_assignments(cell_number, cell_block);
CREATE INDEX idx_cell_assignments_status ON cell_assignments(assignment_status);
CREATE INDEX idx_cell_assignments_date ON cell_assignments(assignment_date DESC);

-- Comments
COMMENT ON TABLE booking_charges IS 'Criminal charges associated with bookings';
COMMENT ON TABLE property_items IS 'Personal property inventory for prisoners at booking';
COMMENT ON TABLE cell_assignments IS 'Cell and bed assignments for prisoners';
