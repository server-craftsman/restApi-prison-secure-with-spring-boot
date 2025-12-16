package vn.gov.prison.secure.application.usecase.workflow;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.workflow.WorkflowInstanceResponse;
import vn.gov.prison.secure.application.mapper.WorkflowMapper;
import vn.gov.prison.secure.domain.model.workflow.WorkflowInstance;
import vn.gov.prison.secure.domain.repository.WorkflowInstanceRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompleteWorkflowStepUseCase {

    private final WorkflowInstanceRepository workflowInstanceRepository;
    private final WorkflowMapper mapper;

    @Transactional
    public WorkflowInstanceResponse execute(UUID instanceId) {
        WorkflowInstance instance = workflowInstanceRepository.findById(instanceId)
                .orElseThrow(() -> new IllegalArgumentException("Workflow instance not found: " + instanceId));

        instance.advanceToNextStep();
        WorkflowInstance updated = workflowInstanceRepository.save(instance);
        return mapper.toResponse(updated);
    }
}
