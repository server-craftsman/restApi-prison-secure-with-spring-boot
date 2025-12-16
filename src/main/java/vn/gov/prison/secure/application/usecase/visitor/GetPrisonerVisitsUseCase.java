package vn.gov.prison.secure.application.usecase.visitor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.visitor.VisitRequestResponse;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetPrisonerVisitsUseCase {

    @Transactional(readOnly = true)
    public List<VisitRequestResponse> execute(UUID prisonerId) {
        // Simple implementation - in real scenario would query database
        return List.of();
    }
}
