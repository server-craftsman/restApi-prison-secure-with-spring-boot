package vn.gov.prison.secure.domain.model.medical;

/**
 * Enumeration of checkup types
 */
public enum CheckupType {
    ADMISSION("Admission checkup - initial health assessment"),
    ROUTINE("Routine checkup - scheduled health monitoring"),
    FOLLOW_UP("Follow-up checkup - reassessment after treatment"),
    EMERGENCY("Emergency checkup - urgent health assessment"),
    PRE_RELEASE("Pre-release checkup - health clearance before release"),
    ANNUAL("Annual checkup - comprehensive yearly health exam");

    private final String description;

    CheckupType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isUrgent() {
        return this == EMERGENCY;
    }
}
