package vn.gov.prison.secure.domain.model.medical;

/**
 * Enumeration of record types for medical records
 */
public enum RecordType {
    GENERAL("General medical visit"),
    EMERGENCY("Emergency medical treatment"),
    ROUTINE_CHECKUP("Routine health checkup"),
    SPECIALIST("Specialist consultation"),
    MENTAL_HEALTH("Mental health assessment"),
    DENTAL("Dental treatment"),
    OTHER("Other medical record");

    private final String description;

    RecordType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
