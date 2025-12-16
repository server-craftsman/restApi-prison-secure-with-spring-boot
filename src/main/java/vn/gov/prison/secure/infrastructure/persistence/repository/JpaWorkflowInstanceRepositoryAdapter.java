package vn.gov.prison.secure.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.gov.prison.secure.domain.model.workflow.WorkflowInstance;
import vn.gov.prison.secure.domain.model.workflow.WorkflowStatus;
import vn.gov.prison.secure.domain.repository.WorkflowInstanceRepository;
import vn.gov.prison.secure.infrastructure.persistence.entity.WorkflowInstanceJpaEntity;
import vn.gov.prison.secure.infrastructure.persistence.mapper.WorkflowPersistenceMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaWorkflowInstanceRepositoryAdapter implements WorkflowInstanceRepository {

    private final SpringDataWorkflowInstanceRepository jpaRepository;
    private final WorkflowPersistenceMapper mapper;

    @Override
    public WorkflowInstance save(WorkflowInstance instance) {
        WorkflowInstanceJpaEntity jpaEntity = mapper.toJpaEntity(instance);
        WorkflowInstanceJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<WorkflowInstance> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<WorkflowInstance> findByPrisonerId(String prisonerId) {
        return jpaRepository.findByPrisonerId(prisonerId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkflowInstance> findByStatus(WorkflowStatus status) {
        WorkflowInstanceJpaEntity.WorkflowStatusEnum statusEnum = WorkflowInstanceJpaEntity.WorkflowStatusEnum
                .valueOf(status.name());
        return jpaRepository.findByStatus(statusEnum).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkflowInstance> findByAssignedTo(String assignedTo) {
        return jpaRepository.findByAssignedTo(assignedTo).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }
}
