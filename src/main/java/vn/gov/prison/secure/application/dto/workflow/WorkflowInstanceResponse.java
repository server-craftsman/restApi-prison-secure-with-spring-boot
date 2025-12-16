package vn.gov.prison.secure.application.dto.workflow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowInstanceResponse {

    private UUID id;
    private UUID workflowId;
    private UUID prisonerId;
    private UUID bookingId;
    private UUID referenceId;
    private String referenceType;
    private String status;
    private Integer currentStep;
    private Integer totalSteps;
    private String priority;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;
    private String initiatedBy;
    private String assignedTo;
    private String metadata;
}
