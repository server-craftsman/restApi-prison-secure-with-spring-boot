package vn.gov.prison.secure.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;
import vn.gov.prison.secure.domain.model.workflow.*;
import vn.gov.prison.secure.infrastructure.persistence.jpa.entity.WorkflowInstanceJpaEntity;
import vn.gov.prison.secure.infrastructure.persistence.jpa.entity.WorkflowJpaEntity;

@Component
public class WorkflowPersistenceMapper {

    public WorkflowJpaEntity toJpaEntity(Workflow domain) {
        if (domain == null)
            return null;

        return WorkflowJpaEntity.builder()
                .id(domain.getWorkflowId().getValue())
                .workflowType(mapWorkflowType(domain.getWorkflowType()))
                .workflowName(domain.getWorkflowName())
                .description(domain.getDescription())
                .isActive(domain.isActive())
                .build();
    }

    public Workflow toDomain(WorkflowJpaEntity jpa) {
        if (jpa == null)
            return null;

        return Workflow.builder()
                .workflowId(WorkflowId.of(jpa.getId()))
                .workflowType(mapWorkflowTypeEnum(jpa.getWorkflowType()))
                .workflowName(jpa.getWorkflowName())
                .description(jpa.getDescription())
                .isActive(jpa.getIsActive() != null && jpa.getIsActive())
                .build();
    }

    public WorkflowInstanceJpaEntity toJpaEntity(WorkflowInstance domain) {
        if (domain == null)
            return null;

        return WorkflowInstanceJpaEntity.builder()
                .id(domain.getId())
                .workflowId(domain.getWorkflowId())
                .prisonerId(domain.getPrisonerId())
                .bookingId(domain.getBookingId())
                .referenceId(domain.getReferenceId())
                .referenceType(domain.getReferenceType())
                .status(mapWorkflowStatus(domain.getStatus()))
                .currentStep(domain.getCurrentStep())
                .totalSteps(domain.getTotalSteps())
                .priority(mapWorkflowPriority(domain.getPriority()))
                .startedAt(domain.getStartedAt())
                .completedAt(domain.getCompletedAt())
                .cancelledAt(domain.getCancelledAt())
                .initiatedBy(domain.getInitiatedBy())
                .assignedTo(domain.getAssignedTo())
                .metadata(domain.getMetadata())
                .build();
    }

    public WorkflowInstance toDomain(WorkflowInstanceJpaEntity jpa) {
        if (jpa == null)
            return null;

        return WorkflowInstance.builder()
                .id(jpa.getId())
                .workflowId(jpa.getWorkflowId())
                .prisonerId(jpa.getPrisonerId())
                .bookingId(jpa.getBookingId())
                .referenceId(jpa.getReferenceId())
                .referenceType(jpa.getReferenceType())
                .status(mapWorkflowStatusEnum(jpa.getStatus()))
                .currentStep(jpa.getCurrentStep())
                .totalSteps(jpa.getTotalSteps())
                .priority(mapWorkflowPriorityEnum(jpa.getPriority()))
                .startedAt(jpa.getStartedAt())
                .completedAt(jpa.getCompletedAt())
                .cancelledAt(jpa.getCancelledAt())
                .initiatedBy(jpa.getInitiatedBy())
                .assignedTo(jpa.getAssignedTo())
                .metadata(jpa.getMetadata())
                .build();
    }

    // Mapping methods
    private WorkflowJpaEntity.WorkflowTypeEnum mapWorkflowType(WorkflowType type) {
        return type != null ? WorkflowJpaEntity.WorkflowTypeEnum.valueOf(type.name()) : null;
    }

    private WorkflowType mapWorkflowTypeEnum(WorkflowJpaEntity.WorkflowTypeEnum type) {
        return type != null ? WorkflowType.valueOf(type.name()) : null;
    }

    private WorkflowInstanceJpaEntity.WorkflowStatusEnum mapWorkflowStatus(WorkflowStatus status) {
        return status != null ? WorkflowInstanceJpaEntity.WorkflowStatusEnum.valueOf(status.name()) : null;
    }

    private WorkflowStatus mapWorkflowStatusEnum(WorkflowInstanceJpaEntity.WorkflowStatusEnum status) {
        return status != null ? WorkflowStatus.valueOf(status.name()) : null;
    }

    private WorkflowInstanceJpaEntity.WorkflowPriorityEnum mapWorkflowPriority(WorkflowPriority priority) {
        return priority != null ? WorkflowInstanceJpaEntity.WorkflowPriorityEnum.valueOf(priority.name()) : null;
    }

    private WorkflowPriority mapWorkflowPriorityEnum(WorkflowInstanceJpaEntity.WorkflowPriorityEnum priority) {
        return priority != null ? WorkflowPriority.valueOf(priority.name()) : null;
    }
}
