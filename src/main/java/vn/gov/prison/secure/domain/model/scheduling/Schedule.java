package vn.gov.prison.secure.domain.model.scheduling;

import lombok.Getter;
import vn.gov.prison.secure.domain.model.common.BaseEntity;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;

import java.time.LocalDateTime;

/**
 * Schedule aggregate root
 * Represents a scheduled event for a prisoner
 */
@Getter
public class Schedule extends BaseEntity<ScheduleId> {
    private final ScheduleId scheduleId;
    private final PrisonerId prisonerId;
    private final ScheduleType scheduleType;
    private String title;
    private String description;
    private LocalDateTime scheduledDate;
    private LocalDateTime endDate;
    private Integer durationMinutes;
    private String location;
    private ScheduleStatus status;
    private SchedulePriority priority;
    private boolean reminderSent;
    private LocalDateTime reminderDate;
    private final String createdBy;

    private Schedule(Builder builder) {
        this.id = builder.id;
        this.scheduleId = builder.id;
        this.prisonerId = builder.prisonerId;
        this.scheduleType = builder.scheduleType;
        this.title = builder.title;
        this.description = builder.description;
        this.scheduledDate = builder.scheduledDate;
        this.endDate = builder.endDate;
        this.durationMinutes = builder.durationMinutes;
        this.location = builder.location;
        this.status = builder.status;
        this.priority = builder.priority;
        this.reminderSent = builder.reminderSent;
        this.reminderDate = builder.reminderDate;
        this.createdBy = builder.createdBy;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Business methods
    public void confirm() {
        if (this.status != ScheduleStatus.SCHEDULED) {
            throw new IllegalStateException("Can only confirm scheduled events");
        }
        this.status = ScheduleStatus.CONFIRMED;
    }

    public void complete() {
        if (this.status != ScheduleStatus.CONFIRMED && this.status != ScheduleStatus.SCHEDULED) {
            throw new IllegalStateException("Can only complete confirmed or scheduled events");
        }
        this.status = ScheduleStatus.COMPLETED;
    }

    public void cancel() {
        if (this.status == ScheduleStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed events");
        }
        this.status = ScheduleStatus.CANCELLED;
    }

    public void reschedule(LocalDateTime newDate, LocalDateTime newEndDate) {
        if (this.status == ScheduleStatus.COMPLETED) {
            throw new IllegalStateException("Cannot reschedule completed events");
        }
        if (newEndDate != null && newEndDate.isBefore(newDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        this.scheduledDate = newDate;
        this.endDate = newEndDate;
        this.status = ScheduleStatus.RESCHEDULED;
        this.reminderSent = false;
    }

    public void markNoShow() {
        if (this.status != ScheduleStatus.CONFIRMED && this.status != ScheduleStatus.SCHEDULED) {
            throw new IllegalStateException("Can only mark no-show for confirmed or scheduled events");
        }
        this.status = ScheduleStatus.NO_SHOW;
    }

    public void sendReminder() {
        this.reminderSent = true;
        this.reminderDate = LocalDateTime.now();
    }

    public void updateDetails(String title, String description, String location) {
        if (this.status == ScheduleStatus.COMPLETED || this.status == ScheduleStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update completed or cancelled events");
        }
        this.title = title;
        this.description = description;
        this.location = location;
    }

    public void updatePriority(SchedulePriority newPriority) {
        this.priority = newPriority;
    }

    public static class Builder {
        private ScheduleId id;
        private PrisonerId prisonerId;
        private ScheduleType scheduleType;
        private String title;
        private String description;
        private LocalDateTime scheduledDate;
        private LocalDateTime endDate;
        private Integer durationMinutes;
        private String location;
        private ScheduleStatus status = ScheduleStatus.SCHEDULED;
        private SchedulePriority priority = SchedulePriority.NORMAL;
        private boolean reminderSent = false;
        private LocalDateTime reminderDate;
        private String createdBy;

        public Builder id(ScheduleId id) {
            this.id = id;
            return this;
        }

        public Builder prisonerId(PrisonerId prisonerId) {
            this.prisonerId = prisonerId;
            return this;
        }

        public Builder scheduleType(ScheduleType scheduleType) {
            this.scheduleType = scheduleType;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder scheduledDate(LocalDateTime scheduledDate) {
            this.scheduledDate = scheduledDate;
            return this;
        }

        public Builder endDate(LocalDateTime endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder durationMinutes(Integer durationMinutes) {
            this.durationMinutes = durationMinutes;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder status(ScheduleStatus status) {
            this.status = status;
            return this;
        }

        public Builder priority(SchedulePriority priority) {
            this.priority = priority;
            return this;
        }

        public Builder reminderSent(boolean reminderSent) {
            this.reminderSent = reminderSent;
            return this;
        }

        public Builder reminderDate(LocalDateTime reminderDate) {
            this.reminderDate = reminderDate;
            return this;
        }

        public Builder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Schedule build() {
            if (scheduleType == null) {
                throw new IllegalStateException("Schedule type is required");
            }
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalStateException("Title is required");
            }
            if (scheduledDate == null) {
                throw new IllegalStateException("Scheduled date is required");
            }
            if (endDate != null && endDate.isBefore(scheduledDate)) {
                throw new IllegalArgumentException("End date cannot be before scheduled date");
            }
            if (createdBy == null || createdBy.trim().isEmpty()) {
                throw new IllegalStateException("Created by is required");
            }

            if (id == null) {
                id = ScheduleId.generate();
            }

            return new Schedule(this);
        }
    }
}
