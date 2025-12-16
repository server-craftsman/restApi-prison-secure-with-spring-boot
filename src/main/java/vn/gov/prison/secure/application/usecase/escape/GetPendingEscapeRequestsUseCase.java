package vn.gov.prison.secure.application.usecase.escape;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.escape.EscapeRequestResponse;
import vn.gov.prison.secure.domain.escape.EscapeRequest;
import vn.gov.prison.secure.domain.escape.EscapeRequestRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetPendingEscapeRequestsUseCase {

    private final EscapeRequestRepository escapeRequestRepository;

    @Transactional(readOnly = true)
    public List<EscapeRequestResponse> execute() {
        List<EscapeRequest> pendingRequests = escapeRequestRepository.findPendingRequests();

        return pendingRequests.stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }

    private EscapeRequestResponse buildResponse(EscapeRequest request) {
        return EscapeRequestResponse.builder()
                .id(request.getId().getValue())
                .prisonerId(request.getPrisonerId().getValue())
                .requestDate(request.getRequestDate())
                .reason(request.getReason())
                .plannedEscapeDate(request.getPlannedEscapeDate())
                .status(request.getStatus().name())
                .build();
    }
}
