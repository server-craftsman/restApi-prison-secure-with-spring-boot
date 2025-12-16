package vn.gov.prison.secure.domain.model.prisoner;

/**
 * Enum representing possible prisoner statuses
 * Following Open/Closed principle - can extend without modifying
 */
public enum PrisonerStatus {
    PENDING_ADMISSION("Pending Admission", "Awaiting processing"),
    ACTIVE("Active", "Currently incarcerated"),
    TEMPORARY_RELEASE("Temporary Release", "Released temporarily"),
    ON_PAROLE("On Parole", "Released on parole"),
    RELEASED("Released", "Fully released"),
    TRANSFERRED("Transferred", "Transferred to another facility"),
    DECEASED("Deceased", "Deceased while in custody"),
    ESCAPED("Escaped", "Escaped from custody");

    private final String displayName;
    private final String description;

    PrisonerStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean canTransitionTo(PrisonerStatus newStatus) {
        return switch (this) {
            case PENDING_ADMISSION -> newStatus == ACTIVE || newStatus == RELEASED;
            case ACTIVE -> newStatus != PENDING_ADMISSION;
            case TEMPORARY_RELEASE -> newStatus == ACTIVE || newStatus == RELEASED;
            case ON_PAROLE -> newStatus == ACTIVE || newStatus == RELEASED;
            case RELEASED -> false; // Cannot transition from released
            case TRANSFERRED -> newStatus == ACTIVE;
            case DECEASED, ESCAPED -> false; // Terminal states
        };
    }
}
