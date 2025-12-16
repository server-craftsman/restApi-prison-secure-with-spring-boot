package vn.gov.prison.secure.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for Workflow Instances
 */
@Entity
@Table(name = "workflow_instances")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowInstanceJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "workflow_id", nullable = false)
    private UUID workflowId;

    @Column(name = "prisoner_id", length = 36)
    private String prisonerId;

    @Column(name = "booking_id", length = 36)
    private String bookingId;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private WorkflowStatusEnum status;

    @Column(name = "current_step", nullable = false)
    private Integer currentStep;

    @Column(name = "total_steps", nullable = false)
    private Integer totalSteps;

    @Column(name = "priority", length = 20)
    @Enumerated(EnumType.STRING)
    private WorkflowPriorityEnum priority;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "initiated_by", nullable = false)
    private String initiatedBy;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "metadata", columnDefinition = "JSONB")
    private String metadata;

    public enum WorkflowStatusEnum {
        INITIATED, IN_PROGRESS, AWAITING_APPROVAL, APPROVED, REJECTED, COMPLETED, CANCELLED, ON_HOLD
    }

    public enum WorkflowPriorityEnum {
        LOW, NORMAL, HIGH, URGENT
    }
}
