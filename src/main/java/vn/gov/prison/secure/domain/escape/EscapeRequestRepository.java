package vn.gov.prison.secure.domain.escape;

import vn.gov.prison.secure.domain.prisoner.PrisonerId;

import java.util.List;
import java.util.Optional;

public interface EscapeRequestRepository {
    EscapeRequest save(EscapeRequest request);

    Optional<EscapeRequest> findById(EscapeRequestId id);

    List<EscapeRequest> findByPrisoner(PrisonerId prisonerId);

    List<EscapeRequest> findByStatus(EscapeRequestStatus status);

    List<EscapeRequest> findPendingRequests();

    List<EscapeRequest> findGuardApprovedRequests();
}
