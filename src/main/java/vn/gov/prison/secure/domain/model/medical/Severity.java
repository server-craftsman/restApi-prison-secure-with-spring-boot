package vn.gov.prison.secure.domain.model.medical;

/**
 * Enumeration of severity levels for medical conditions
 */
public enum Severity {
    LOW("Low severity - routine care"),
    MEDIUM("Medium severity - monitoring required"),
    HIGH("High severity - immediate attention needed"),
    CRITICAL("Critical - emergency response required");

    private final String description;

    Severity(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean requiresUrgentCare() {
        return this == HIGH || this == CRITICAL;
    }
}
