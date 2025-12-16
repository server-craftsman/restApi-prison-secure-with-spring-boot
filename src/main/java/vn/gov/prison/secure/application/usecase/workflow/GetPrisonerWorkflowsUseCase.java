package vn.gov.prison.secure.application.usecase.workflow;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.workflow.WorkflowInstanceResponse;
import vn.gov.prison.secure.application.mapper.WorkflowMapper;
import vn.gov.prison.secure.domain.repository.WorkflowInstanceRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetPrisonerWorkflowsUseCase {

    private final WorkflowInstanceRepository workflowInstanceRepository;
    private final WorkflowMapper mapper;

    @Transactional(readOnly = true)
    public List<WorkflowInstanceResponse> execute(UUID prisonerId) {
        return workflowInstanceRepository.findByPrisonerId(prisonerId.toString())
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
