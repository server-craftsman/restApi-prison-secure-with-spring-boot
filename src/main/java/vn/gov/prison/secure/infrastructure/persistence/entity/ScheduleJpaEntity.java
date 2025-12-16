package vn.gov.prison.secure.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for Schedules
 */
@Entity
@Table(name = "schedules")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "prisoner_id", length = 36)
    private String prisonerId;

    @Column(name = "schedule_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ScheduleTypeEnum scheduleType;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDateTime scheduledDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "location")
    private String location;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ScheduleStatusEnum status;

    @Column(name = "priority", length = 20)
    @Enumerated(EnumType.STRING)
    private SchedulePriorityEnum priority;

    @Column(name = "reminder_sent")
    private Boolean reminderSent;

    @Column(name = "reminder_date")
    private LocalDateTime reminderDate;

    public enum ScheduleTypeEnum {
        COURT, MEDICAL, VISITOR, ACTIVITY, TRANSFER, RELEASE, OTHER
    }

    public enum ScheduleStatusEnum {
        SCHEDULED, CONFIRMED, COMPLETED, CANCELLED, RESCHEDULED, NO_SHOW
    }

    public enum SchedulePriorityEnum {
        LOW, NORMAL, HIGH, URGENT
    }
}
