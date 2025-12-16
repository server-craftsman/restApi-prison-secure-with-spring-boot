package vn.gov.prison.secure.application.usecase.document;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.document.DocumentResponse;
import vn.gov.prison.secure.application.dto.document.SearchDocumentsRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchDocumentsUseCase {

    @Transactional(readOnly = true)
    public List<DocumentResponse> execute(SearchDocumentsRequest request) {
        // Simple implementation - in real scenario would search database
        return List.of();
    }
}
