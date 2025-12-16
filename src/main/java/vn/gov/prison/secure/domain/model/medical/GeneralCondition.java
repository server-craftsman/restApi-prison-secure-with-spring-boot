package vn.gov.prison.secure.domain.model.medical;

/**
 * Enumeration of general health condition assessments
 */
public enum GeneralCondition {
    EXCELLENT("Excellent - no health concerns"),
    GOOD("Good - minor or well-managed conditions"),
    FAIR("Fair - some health concerns requiring monitoring"),
    POOR("Poor - significant health issues"),
    CRITICAL("Critical - immediate medical attention required");

    private final String description;

    GeneralCondition(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean requiresMedicalAttention() {
        return this == POOR || this == CRITICAL;
    }
}
