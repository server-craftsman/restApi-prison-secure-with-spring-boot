package vn.gov.prison.secure.application.usecase.document;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.document.DocumentResponse;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetPrisonerDocumentsUseCase {

    @Transactional(readOnly = true)
    public List<DocumentResponse> execute(UUID prisonerId) {
        // Simple implementation - in real scenario would query database
        return List.of();
    }
}
