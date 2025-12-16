package vn.gov.prison.secure.application.dto.workflow;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitiateWorkflowRequest {

    @NotNull(message = "Workflow ID is required")
    private UUID workflowId;

    private UUID prisonerId;

    private UUID bookingId;

    private UUID referenceId;

    private String referenceType;

    @NotNull(message = "Total steps is required")
    private Integer totalSteps;

    private String priority;

    @NotBlank(message = "Initiated by is required")
    private String initiatedBy;

    private String assignedTo;

    private String metadata;
}
