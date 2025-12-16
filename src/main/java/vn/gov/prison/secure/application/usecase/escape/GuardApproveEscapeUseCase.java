package vn.gov.prison.secure.application.usecase.escape;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.escape.ApproveEscapeRequestRequest;
import vn.gov.prison.secure.application.dto.escape.EscapeRequestResponse;
import vn.gov.prison.secure.domain.escape.EscapeRequest;
import vn.gov.prison.secure.domain.escape.EscapeRequestId;
import vn.gov.prison.secure.domain.escape.EscapeRequestRepository;
import vn.gov.prison.secure.domain.user.UserId;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuardApproveEscapeUseCase {

    private final EscapeRequestRepository escapeRequestRepository;

    @Transactional
    public EscapeRequestResponse execute(UUID requestId, UUID guardId, ApproveEscapeRequestRequest request) {
        EscapeRequestId id = EscapeRequestId.of(requestId);
        UserId guard = UserId.of(guardId);

        // Find escape request
        EscapeRequest escapeRequest = escapeRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Escape request not found"));

        // Approve or reject
        if (request.getApproved()) {
            escapeRequest.approveByGuard(guard, request.getNotes());
        } else {
            escapeRequest.rejectByGuard(guard, request.getNotes());
        }

        // Save
        EscapeRequest saved = escapeRequestRepository.save(escapeRequest);

        // Build response
        return buildResponse(saved);
    }

    private EscapeRequestResponse buildResponse(EscapeRequest request) {
        return EscapeRequestResponse.builder()
                .id(request.getId().getValue())
                .prisonerId(request.getPrisonerId().getValue())
                .requestDate(request.getRequestDate())
                .reason(request.getReason())
                .plannedEscapeDate(request.getPlannedEscapeDate())
                .status(request.getStatus().name())
                .guardApprovalDate(request.getGuardApprovalDate())
                .guardApprovalNotes(request.getGuardApprovalNotes())
                .build();
    }
}
