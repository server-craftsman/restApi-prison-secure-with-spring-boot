package vn.gov.prison.secure.application.usecase.document;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteDocumentUseCase {

    @Transactional
    public void execute(UUID documentId) {
        // Simple implementation - in real scenario would delete from database and
        // storage
    }
}
