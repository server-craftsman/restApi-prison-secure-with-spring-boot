package vn.gov.prison.secure.domain.model.workflow;

import lombok.Getter;
import vn.gov.prison.secure.domain.model.common.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * WorkflowInstance entity
 * Represents an execution of a workflow
 */
@Getter
public class WorkflowInstance extends BaseEntity<UUID> {
    private final UUID workflowId;
    private final String prisonerId;
    private final String bookingId;
    private final UUID referenceId;
    private final String referenceType;
    private WorkflowStatus status;
    private int currentStep;
    private final int totalSteps;
    private WorkflowPriority priority;
    private final LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;
    private final String initiatedBy;
    private String assignedTo;
    private String metadata;

    private WorkflowInstance(Builder builder) {
        this.id = builder.id;
        this.workflowId = builder.workflowId;
        this.prisonerId = builder.prisonerId;
        this.bookingId = builder.bookingId;
        this.referenceId = builder.referenceId;
        this.referenceType = builder.referenceType;
        this.status = builder.status;
        this.currentStep = builder.currentStep;
        this.totalSteps = builder.totalSteps;
        this.priority = builder.priority;
        this.startedAt = builder.startedAt;
        this.completedAt = builder.completedAt;
        this.cancelledAt = builder.cancelledAt;
        this.initiatedBy = builder.initiatedBy;
        this.assignedTo = builder.assignedTo;
        this.metadata = builder.metadata;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Business methods
    public void start() {
        if (this.status != WorkflowStatus.INITIATED) {
            throw new IllegalStateException("Can only start initiated workflows");
        }
        this.status = WorkflowStatus.IN_PROGRESS;
    }

    public void advanceToNextStep() {
        if (this.currentStep >= this.totalSteps) {
            throw new IllegalStateException("Already at final step");
        }
        this.currentStep++;
        if (this.currentStep == this.totalSteps) {
            this.status = WorkflowStatus.AWAITING_APPROVAL;
        }
    }

    public void approve() {
        if (this.status != WorkflowStatus.AWAITING_APPROVAL) {
            throw new IllegalStateException("Can only approve workflows awaiting approval");
        }
        this.status = WorkflowStatus.APPROVED;
    }

    public void reject() {
        if (this.status != WorkflowStatus.AWAITING_APPROVAL) {
            throw new IllegalStateException("Can only reject workflows awaiting approval");
        }
        this.status = WorkflowStatus.REJECTED;
    }

    public void complete() {
        if (this.status != WorkflowStatus.APPROVED && this.status != WorkflowStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only complete approved or in-progress workflows");
        }
        this.status = WorkflowStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status == WorkflowStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed workflows");
        }
        this.status = WorkflowStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
    }

    public void putOnHold() {
        if (this.status == WorkflowStatus.COMPLETED || this.status == WorkflowStatus.CANCELLED) {
            throw new IllegalStateException("Cannot put completed or cancelled workflows on hold");
        }
        this.status = WorkflowStatus.ON_HOLD;
    }

    public void resume() {
        if (this.status != WorkflowStatus.ON_HOLD) {
            throw new IllegalStateException("Can only resume workflows on hold");
        }
        this.status = WorkflowStatus.IN_PROGRESS;
    }

    public void assignTo(String assignee) {
        this.assignedTo = assignee;
    }

    public void updatePriority(WorkflowPriority newPriority) {
        this.priority = newPriority;
    }

    public static class Builder {
        private UUID id;
        private UUID workflowId;
        private String prisonerId;
        private String bookingId;
        private UUID referenceId;
        private String referenceType;
        private WorkflowStatus status = WorkflowStatus.INITIATED;
        private int currentStep = 1;
        private int totalSteps;
        private WorkflowPriority priority = WorkflowPriority.NORMAL;
        private LocalDateTime startedAt = LocalDateTime.now();
        private LocalDateTime completedAt;
        private LocalDateTime cancelledAt;
        private String initiatedBy;
        private String assignedTo;
        private String metadata;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder workflowId(UUID workflowId) {
            this.workflowId = workflowId;
            return this;
        }

        public Builder prisonerId(String prisonerId) {
            this.prisonerId = prisonerId;
            return this;
        }

        public Builder bookingId(String bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public Builder referenceId(UUID referenceId) {
            this.referenceId = referenceId;
            return this;
        }

        public Builder referenceType(String referenceType) {
            this.referenceType = referenceType;
            return this;
        }

        public Builder status(WorkflowStatus status) {
            this.status = status;
            return this;
        }

        public Builder currentStep(int currentStep) {
            this.currentStep = currentStep;
            return this;
        }

        public Builder totalSteps(int totalSteps) {
            this.totalSteps = totalSteps;
            return this;
        }

        public Builder priority(WorkflowPriority priority) {
            this.priority = priority;
            return this;
        }

        public Builder startedAt(LocalDateTime startedAt) {
            this.startedAt = startedAt;
            return this;
        }

        public Builder completedAt(LocalDateTime completedAt) {
            this.completedAt = completedAt;
            return this;
        }

        public Builder cancelledAt(LocalDateTime cancelledAt) {
            this.cancelledAt = cancelledAt;
            return this;
        }

        public Builder initiatedBy(String initiatedBy) {
            this.initiatedBy = initiatedBy;
            return this;
        }

        public Builder assignedTo(String assignedTo) {
            this.assignedTo = assignedTo;
            return this;
        }

        public Builder metadata(String metadata) {
            this.metadata = metadata;
            return this;
        }

        public WorkflowInstance build() {
            if (workflowId == null) {
                throw new IllegalStateException("Workflow ID is required");
            }
            if (totalSteps <= 0) {
                throw new IllegalStateException("Total steps must be positive");
            }
            if (initiatedBy == null || initiatedBy.trim().isEmpty()) {
                throw new IllegalStateException("Initiated by is required");
            }

            if (id == null) {
                id = UUID.randomUUID();
            }

            return new WorkflowInstance(this);
        }
    }
}
