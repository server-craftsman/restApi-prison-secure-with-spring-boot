package vn.gov.prison.secure.application.usecase.escape;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.escape.CreateEscapeRequestRequest;
import vn.gov.prison.secure.application.dto.escape.EscapeRequestResponse;
import vn.gov.prison.secure.domain.escape.EscapeRequest;
import vn.gov.prison.secure.domain.escape.EscapeRequestRepository;
import vn.gov.prison.secure.domain.prisoner.PrisonerId;

@Service
@RequiredArgsConstructor
public class CreateEscapeRequestUseCase {

    private final EscapeRequestRepository escapeRequestRepository;

    @Transactional
    public EscapeRequestResponse execute(CreateEscapeRequestRequest request) {
        PrisonerId prisonerId = PrisonerId.of(request.getPrisonerId());

        // Create escape request
        EscapeRequest escapeRequest = EscapeRequest.create(
                prisonerId,
                request.getReason(),
                request.getPlannedEscapeDate());

        // Save
        EscapeRequest saved = escapeRequestRepository.save(escapeRequest);

        // Build response
        return EscapeRequestResponse.builder()
                .id(saved.getId().getValue())
                .prisonerId(saved.getPrisonerId().getValue())
                .requestDate(saved.getRequestDate())
                .reason(saved.getReason())
                .plannedEscapeDate(saved.getPlannedEscapeDate())
                .status(saved.getStatus().name())
                .build();
    }
}
