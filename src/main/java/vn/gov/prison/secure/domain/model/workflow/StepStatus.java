package vn.gov.prison.secure.domain.model.workflow;

/**
 * Enum representing workflow step status
 */
public enum StepStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    SKIPPED,
    FAILED,
    AWAITING_APPROVAL
}
