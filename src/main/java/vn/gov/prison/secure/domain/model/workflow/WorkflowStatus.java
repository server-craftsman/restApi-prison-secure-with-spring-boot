package vn.gov.prison.secure.domain.model.workflow;

/**
 * Enum representing workflow instance status
 */
public enum WorkflowStatus {
    INITIATED,
    IN_PROGRESS,
    AWAITING_APPROVAL,
    APPROVED,
    REJECTED,
    COMPLETED,
    CANCELLED,
    ON_HOLD
}
