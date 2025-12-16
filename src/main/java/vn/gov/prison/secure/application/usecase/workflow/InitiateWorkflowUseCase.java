package vn.gov.prison.secure.application.usecase.workflow;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.workflow.InitiateWorkflowRequest;
import vn.gov.prison.secure.application.dto.workflow.WorkflowInstanceResponse;
import vn.gov.prison.secure.application.mapper.WorkflowMapper;
import vn.gov.prison.secure.domain.model.workflow.WorkflowInstance;
import vn.gov.prison.secure.domain.repository.WorkflowInstanceRepository;

@Service
@RequiredArgsConstructor
public class InitiateWorkflowUseCase {

    private final WorkflowInstanceRepository workflowInstanceRepository;
    private final WorkflowMapper mapper;

    @Transactional
    public WorkflowInstanceResponse execute(InitiateWorkflowRequest request) {
        WorkflowInstance instance = mapper.toDomain(request);
        WorkflowInstance saved = workflowInstanceRepository.save(instance);
        return mapper.toResponse(saved);
    }
}
