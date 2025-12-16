package vn.gov.prison.secure.domain.repository;

import vn.gov.prison.secure.domain.model.workflow.WorkflowInstance;
import vn.gov.prison.secure.domain.model.workflow.WorkflowStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Domain repository interface for WorkflowInstance
 */
public interface WorkflowInstanceRepository {

    WorkflowInstance save(WorkflowInstance instance);

    Optional<WorkflowInstance> findById(UUID id);

    List<WorkflowInstance> findByPrisonerId(String prisonerId);

    List<WorkflowInstance> findByStatus(WorkflowStatus status);

    List<WorkflowInstance> findByAssignedTo(String assignedTo);

    void delete(UUID id);

    boolean existsById(UUID id);
}
