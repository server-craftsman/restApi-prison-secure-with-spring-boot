package vn.gov.prison.secure.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.gov.prison.secure.infrastructure.persistence.entity.WorkflowInstanceJpaEntity;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for Workflow Instances
 */
@Repository
public interface SpringDataWorkflowInstanceRepository extends JpaRepository<WorkflowInstanceJpaEntity, UUID> {

    /**
     * Find all workflow instances for a prisoner
     */
    List<WorkflowInstanceJpaEntity> findByPrisonerId(String prisonerId);

    /**
     * Find workflow instances by status
     */
    List<WorkflowInstanceJpaEntity> findByStatus(WorkflowInstanceJpaEntity.WorkflowStatusEnum status);

    /**
     * Find workflow instances assigned to a user
     */
    List<WorkflowInstanceJpaEntity> findByAssignedTo(String assignedTo);

    /**
     * Find workflow instances by workflow ID
     */
    List<WorkflowInstanceJpaEntity> findByWorkflowId(UUID workflowId);

    /**
     * Find workflow instances by prisoner and status
     */
    List<WorkflowInstanceJpaEntity> findByPrisonerIdAndStatus(
            String prisonerId,
            WorkflowInstanceJpaEntity.WorkflowStatusEnum status);
}
