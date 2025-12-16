package vn.gov.prison.secure.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.gov.prison.secure.application.dto.document.DocumentResponse;
import vn.gov.prison.secure.application.dto.document.SearchDocumentsRequest;
import vn.gov.prison.secure.application.dto.document.UploadDocumentRequest;
import vn.gov.prison.secure.application.usecase.document.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Tag(name = "Records Management", description = "APIs for managing prisoner documents")
public class DocumentController {

    private final UploadDocumentUseCase uploadDocumentUseCase;
    private final GetDocumentUseCase getDocumentUseCase;
    private final SearchDocumentsUseCase searchDocumentsUseCase;
    private final DeleteDocumentUseCase deleteDocumentUseCase;
    private final GetPrisonerDocumentsUseCase getPrisonerDocumentsUseCase;

    @PostMapping("/upload")
    @Operation(summary = "Upload document")
    public ResponseEntity<DocumentResponse> uploadDocument(@RequestBody UploadDocumentRequest request) {
        DocumentResponse response = uploadDocumentUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get document by ID")
    public ResponseEntity<DocumentResponse> getDocument(@PathVariable UUID id) {
        DocumentResponse response = getDocumentUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    @Operation(summary = "Search documents")
    public ResponseEntity<List<DocumentResponse>> searchDocuments(@RequestBody SearchDocumentsRequest request) {
        List<DocumentResponse> documents = searchDocumentsUseCase.execute(request);
        return ResponseEntity.ok(documents);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete document")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        deleteDocumentUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/prisoners/{prisonerId}")
    @Operation(summary = "Get prisoner documents")
    public ResponseEntity<List<DocumentResponse>> getPrisonerDocuments(@PathVariable UUID prisonerId) {
        List<DocumentResponse> documents = getPrisonerDocumentsUseCase.execute(prisonerId);
        return ResponseEntity.ok(documents);
    }
}
