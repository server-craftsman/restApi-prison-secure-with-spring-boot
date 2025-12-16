-- V2: Create Booking Schema
-- Migration for booking and intake management

CREATE TABLE IF NOT EXISTS bookings (
    id VARCHAR(36) PRIMARY KEY,
    prisoner_id VARCHAR(36) NOT NULL,
    booking_number VARCHAR(50) NOT NULL UNIQUE,
    
    booking_date TIMESTAMP NOT NULL,
    booking_type VARCHAR(30) NOT NULL CHECK (booking_type IN (
        'NEW_ARREST', 'VIOLATION', 'WARRANT', 'TRANSFER', 'COURT_ORDER'
    )),
    booking_status VARCHAR(30) NOT NULL CHECK (booking_status IN (
        'PENDING', 'ACTIVE', 'RELEASED', 'CANCELLED', 'ON_HOLD'
    )),
    
    -- Arrest Information
    arresting_agency VARCHAR(200),
    arrest_location VARCHAR(500),
    arrest_date TIMESTAMP,
    
    -- Bail Information
    bail_amount VARCHAR(50),
    bail_status VARCHAR(20) CHECK (bail_status IN (
        'NOT_SET', 'SET', 'POSTED', 'DENIED', 'REVOKED'
    )),
    
    -- Release Information
    expected_release_date TIMESTAMP,
    actual_release_date TIMESTAMP,
    release_reason TEXT,
    
    -- Audit Fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_booking_prisoner FOREIGN KEY (prisoner_id) 
        REFERENCES prisoners(id) ON DELETE RESTRICT
);

CREATE INDEX idx_booking_prisoner ON bookings(prisoner_id);
CREATE INDEX idx_booking_number ON bookings(booking_number);
CREATE INDEX idx_booking_status ON bookings(booking_status);
CREATE INDEX idx_booking_date ON bookings(booking_date);

-- Charges table
CREATE TABLE IF NOT EXISTS charges (
    id VARCHAR(36) PRIMARY KEY,
    booking_id VARCHAR(36) NOT NULL,
    
    charge_code VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    severity VARCHAR(20) NOT NULL CHECK (severity IN (
        'INFRACTION', 'MISDEMEANOR', 'FELONY', 'CAPITAL'
    )),
    statute VARCHAR(200),
    charge_date TIMESTAMP NOT NULL,
    arresting_officer VARCHAR(200),
    jurisdiction VARCHAR(200),
    charge_status VARCHAR(20) CHECK (charge_status IN (
        'PENDING', 'CONVICTED', 'ACQUITTED', 'DISMISSED', 'REDUCED'
    )),
    
    -- Audit Fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_charge_booking FOREIGN KEY (booking_id) 
        REFERENCES bookings(id) ON DELETE CASCADE
);

CREATE INDEX idx_charge_booking ON charges(booking_id);
CREATE INDEX idx_charge_code ON charges(charge_code);
CREATE INDEX idx_charge_severity ON charges(severity);

-- Court Appearances table
CREATE TABLE IF NOT EXISTS court_appearances (
    id VARCHAR(36) PRIMARY KEY,
    booking_id VARCHAR(36) NOT NULL,
    
    scheduled_date TIMESTAMP NOT NULL,
    court_name VARCHAR(200) NOT NULL,
    court_room VARCHAR(50),
    judge VARCHAR(200),
    prosecutor VARCHAR(200),
    defense_attorney VARCHAR(200),
    
    appearance_type VARCHAR(30) NOT NULL CHECK (appearance_type IN (
        'ARRAIGNMENT', 'PRELIMINARY_HEARING', 'BAIL_HEARING',
        'TRIAL', 'SENTENCING', 'APPEAL', 'PAROLE_HEARING'
    )),
    appearance_status VARCHAR(20) CHECK (appearance_status IN (
        'SCHEDULED', 'COMPLETED', 'CANCELLED', 'POSTPONED', 'IN_PROGRESS'
    )),
    
    outcome VARCHAR(500),
    notes TEXT,
    
    -- Audit Fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_court_booking FOREIGN KEY (booking_id) 
        REFERENCES bookings(id) ON DELETE CASCADE
);

CREATE INDEX idx_court_booking ON court_appearances(booking_id);
CREATE INDEX idx_court_scheduled ON court_appearances(scheduled_date);
CREATE INDEX idx_court_status ON court_appearances(appearance_status);

COMMENT ON TABLE bookings IS 'Booking records for prisoner intake and processing';
COMMENT ON TABLE charges IS 'Criminal charges associated with bookings';
COMMENT ON TABLE court_appearances IS 'Scheduled and completed court appearances';
