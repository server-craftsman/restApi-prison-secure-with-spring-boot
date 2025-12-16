package vn.gov.prison.secure.domain.model.medical;

/**
 * Enumeration of prescription statuses
 */
public enum PrescriptionStatus {
    ACTIVE("Active - currently being administered"),
    COMPLETED("Completed - prescription finished"),
    DISCONTINUED("Discontinued - stopped by medical staff"),
    ON_HOLD("On hold - temporarily suspended");

    private final String description;

    PrescriptionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
