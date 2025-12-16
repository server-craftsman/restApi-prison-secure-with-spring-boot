package vn.gov.prison.secure.application.mapper;

import org.springframework.stereotype.Component;
import vn.gov.prison.secure.application.dto.workflow.InitiateWorkflowRequest;
import vn.gov.prison.secure.application.dto.workflow.WorkflowInstanceResponse;
import vn.gov.prison.secure.domain.model.workflow.WorkflowInstance;
import vn.gov.prison.secure.domain.model.workflow.WorkflowPriority;

import java.util.UUID;

@Component
public class WorkflowMapper {

    public WorkflowInstance toDomain(InitiateWorkflowRequest request) {
        return WorkflowInstance.builder()
                .workflowId(request.getWorkflowId())
                .prisonerId(request.getPrisonerId() != null ? request.getPrisonerId().toString() : null)
                .bookingId(request.getBookingId() != null ? request.getBookingId().toString() : null)
                .referenceId(request.getReferenceId())
                .referenceType(request.getReferenceType())
                .totalSteps(request.getTotalSteps())
                .priority(request.getPriority() != null ? WorkflowPriority.valueOf(request.getPriority())
                        : WorkflowPriority.NORMAL)
                .initiatedBy(request.getInitiatedBy())
                .assignedTo(request.getAssignedTo())
                .metadata(request.getMetadata())
                .build();
    }

    public WorkflowInstanceResponse toResponse(WorkflowInstance domain) {
        return WorkflowInstanceResponse.builder()
                .id(domain.getId())
                .workflowId(domain.getWorkflowId())
                .prisonerId(domain.getPrisonerId() != null ? UUID.fromString(domain.getPrisonerId()) : null)
                .bookingId(domain.getBookingId() != null ? UUID.fromString(domain.getBookingId()) : null)
                .referenceId(domain.getReferenceId())
                .referenceType(domain.getReferenceType())
                .status(domain.getStatus().name())
                .currentStep(domain.getCurrentStep())
                .totalSteps(domain.getTotalSteps())
                .priority(domain.getPriority().name())
                .startedAt(domain.getStartedAt())
                .completedAt(domain.getCompletedAt())
                .cancelledAt(domain.getCancelledAt())
                .initiatedBy(domain.getInitiatedBy())
                .assignedTo(domain.getAssignedTo())
                .metadata(domain.getMetadata())
                .build();
    }
}
