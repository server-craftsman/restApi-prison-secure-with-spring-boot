package vn.gov.prison.secure.domain.model.booking;

import vn.gov.prison.secure.domain.model.common.BaseEntity;

import java.time.LocalDateTime;

/**
 * Court Appearance Entity
 * Part of Booking aggregate
 */
public class CourtAppearance extends BaseEntity<String> {

    private LocalDateTime scheduledDate;
    private String courtName;
    private String courtRoom;
    private String judge;
    private String prosecutor;
    private String defenseAttorney;
    private AppearanceType type;
    private AppearanceStatus status;
    private String outcome;
    private String notes;

    protected CourtAppearance() {
        super();
        this.status = AppearanceStatus.SCHEDULED;
    }

    public static CourtAppearance schedule(LocalDateTime scheduledDate,
            String courtName,
            AppearanceType type) {
        if (scheduledDate == null) {
            throw new IllegalArgumentException("Scheduled date is required");
        }
        if (courtName == null || courtName.trim().isEmpty()) {
            throw new IllegalArgumentException("Court name is required");
        }

        CourtAppearance appearance = new CourtAppearance();
        appearance.id = java.util.UUID.randomUUID().toString();
        appearance.scheduledDate = scheduledDate;
        appearance.courtName = courtName;
        appearance.type = type;

        return appearance;
    }

    public void complete(String outcome) {
        this.status = AppearanceStatus.COMPLETED;
        this.outcome = outcome;
        updateTimestamp();
    }

    public void cancel(String reason) {
        this.status = AppearanceStatus.CANCELLED;
        this.notes = reason;
        updateTimestamp();
    }

    public void postpone(LocalDateTime newDate) {
        if (newDate == null) {
            throw new IllegalArgumentException("New date is required");
        }
        this.scheduledDate = newDate;
        this.status = AppearanceStatus.POSTPONED;
        updateTimestamp();
    }

    // Getters and setters
    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public String getCourtName() {
        return courtName;
    }

    public String getCourtRoom() {
        return courtRoom;
    }

    public void setCourtRoom(String courtRoom) {
        this.courtRoom = courtRoom;
        updateTimestamp();
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
        updateTimestamp();
    }

    public String getProsecutor() {
        return prosecutor;
    }

    public void setProsecutor(String prosecutor) {
        this.prosecutor = prosecutor;
        updateTimestamp();
    }

    public String getDefenseAttorney() {
        return defenseAttorney;
    }

    public void setDefenseAttorney(String defenseAttorney) {
        this.defenseAttorney = defenseAttorney;
        updateTimestamp();
    }

    public AppearanceType getType() {
        return type;
    }

    public AppearanceStatus getStatus() {
        return status;
    }

    public String getOutcome() {
        return outcome;
    }

    public String getNotes() {
        return notes;
    }

    public enum AppearanceType {
        ARRAIGNMENT("Arraignment"),
        PRELIMINARY_HEARING("Preliminary Hearing"),
        BAIL_HEARING("Bail Hearing"),
        TRIAL("Trial"),
        SENTENCING("Sentencing"),
        APPEAL("Appeal"),
        PAROLE_HEARING("Parole Hearing");

        private final String displayName;

        AppearanceType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum AppearanceStatus {
        SCHEDULED, COMPLETED, CANCELLED, POSTPONED, IN_PROGRESS
    }
}
