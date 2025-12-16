package vn.gov.prison.secure.application.usecase.document;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.document.DocumentResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetDocumentUseCase {

    @Transactional(readOnly = true)
    public DocumentResponse execute(UUID documentId) {
        // Simple implementation - in real scenario would retrieve from database
        return DocumentResponse.builder()
                .id(documentId)
                .status("ACTIVE")
                .build();
    }
}
