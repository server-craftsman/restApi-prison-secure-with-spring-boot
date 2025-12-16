package vn.gov.prison.secure.application.usecase.document;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.document.DocumentResponse;
import vn.gov.prison.secure.application.dto.document.UploadDocumentRequest;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadDocumentUseCase {

    @Transactional
    public DocumentResponse execute(UploadDocumentRequest request) {
        // Simple implementation - in real scenario would save file and metadata to
        // storage/database
        return DocumentResponse.builder()
                .id(UUID.randomUUID())
                .prisonerId(request.getPrisonerId())
                .documentType(request.getDocumentType())
                .category(request.getCategory())
                .title(request.getTitle())
                .description(request.getDescription())
                .fileName(request.getFileName())
                .fileUrl(request.getFileUrl())
                .fileSize(request.getFileSize())
                .uploadedAt(LocalDateTime.now())
                .uploadedBy(request.getUploadedBy())
                .status("ACTIVE")
                .build();
    }
}
