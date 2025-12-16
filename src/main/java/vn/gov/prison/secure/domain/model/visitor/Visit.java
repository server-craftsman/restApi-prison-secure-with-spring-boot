package vn.gov.prison.secure.domain.model.visitor;

import vn.gov.prison.secure.domain.model.common.BaseEntity;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;

import java.time.LocalDateTime;

/**
 * Visit Entity representing individual visit instances
 * Part of Visitor aggregate
 */
public class Visit extends BaseEntity<String> {

    private PrisonerId prisonerId;
    private LocalDateTime scheduledDate;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    private VisitType type;
    private VisitStatus status;
    private String location;
    private String supervisingOfficer;
    private String notes;
    private String cancellationReason;

    protected Visit() {
        super();
        this.status = VisitStatus.SCHEDULED;
    }

    public static Visit schedule(PrisonerId prisonerId,
            LocalDateTime scheduledDate,
            VisitType type,
            String location) {
        if (prisonerId == null) {
            throw new IllegalArgumentException("Prisoner ID is required");
        }
        if (scheduledDate == null) {
            throw new IllegalArgumentException("Scheduled date is required");
        }
        if (scheduledDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot schedule visit in the past");
        }

        Visit visit = new Visit();
        visit.id = java.util.UUID.randomUUID().toString();
        visit.prisonerId = prisonerId;
        visit.scheduledDate = scheduledDate;
        visit.type = type;
        visit.location = location;

        return visit;
    }

    public void checkIn() {
        if (status != VisitStatus.SCHEDULED) {
            throw new IllegalStateException("Can only check in scheduled visits");
        }
        this.status = VisitStatus.IN_PROGRESS;
        this.actualStartTime = LocalDateTime.now();
        updateTimestamp();
    }

    public void complete() {
        if (status != VisitStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only complete in-progress visits");
        }
        this.status = VisitStatus.COMPLETED;
        this.actualEndTime = LocalDateTime.now();
        updateTimestamp();
    }

    public void cancel(String reason) {
        if (status == VisitStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed visit");
        }
        this.status = VisitStatus.CANCELLED;
        this.cancellationReason = reason;
        updateTimestamp();
    }

    // Getters and setters
    public PrisonerId getPrisonerId() {
        return prisonerId;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public LocalDateTime getActualStartTime() {
        return actualStartTime;
    }

    public LocalDateTime getActualEndTime() {
        return actualEndTime;
    }

    public VisitType getType() {
        return type;
    }

    public VisitStatus getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public String getSupervisingOfficer() {
        return supervisingOfficer;
    }

    public void setSupervisingOfficer(String supervisingOfficer) {
        this.supervisingOfficer = supervisingOfficer;
        updateTimestamp();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
        updateTimestamp();
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public enum VisitType {
        FAMILY("Family Visit"),
        ATTORNEY("Attorney Visit"),
        CLERGY("Clergy Visit"),
        MEDICAL("Medical Visit"),
        OFFICIAL("Official Visit");

        private final String displayName;

        VisitType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum VisitStatus {
        SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW
    }
}
