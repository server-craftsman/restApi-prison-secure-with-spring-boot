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
@Tag(name = "Records Management - Quản lý Hồ sơ", description = "API quản lý tài liệu, hồ sơ pháp lý và y tế của tù nhân")
public class DocumentController {

    private final UploadDocumentUseCase uploadDocumentUseCase;
    private final GetDocumentUseCase getDocumentUseCase;
    private final SearchDocumentsUseCase searchDocumentsUseCase;
    private final DeleteDocumentUseCase deleteDocumentUseCase;
    private final GetPrisonerDocumentsUseCase getPrisonerDocumentsUseCase;

    @PostMapping("/upload")
    @Operation(summary = "[DOC-001] Tải lên tài liệu/hồ sơ", description = "Tải lên tài liệu mới cho tù nhân bao gồm hồ sơ pháp lý, y tế, hành chính. "
            +
            "Tài liệu sẽ được phân loại và lưu trữ an toàn trong hệ thống.")
    public ResponseEntity<DocumentResponse> uploadDocument(@RequestBody UploadDocumentRequest request) {
        DocumentResponse response = uploadDocumentUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "[DOC-002] Xem chi tiết tài liệu", description = "Lấy thông tin chi tiết của tài liệu theo ID bao gồm metadata, người tải lên, ngày tải")
    public ResponseEntity<DocumentResponse> getDocument(@PathVariable UUID id) {
        DocumentResponse response = getDocumentUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    @Operation(summary = "[DOC-003] Tìm kiếm tài liệu", description = "Tìm kiếm tài liệu theo nhiều tiêu chí: loại tài liệu, danh mục, từ khóa, tù nhân. "
            +
            "Hỗ trợ phân trang để xử lý số lượng lớn kết quả.")
    public ResponseEntity<List<DocumentResponse>> searchDocuments(@RequestBody SearchDocumentsRequest request) {
        List<DocumentResponse> documents = searchDocumentsUseCase.execute(request);
        return ResponseEntity.ok(documents);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "[DOC-004] Xóa tài liệu", description = "Xóa tài liệu khỏi hệ thống (chỉ dành cho admin). " +
            "Thao tác này không thể hoàn tác, cần xác nhận kỹ trước khi thực hiện.")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        deleteDocumentUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/prisoners/{prisonerId}")
    @Operation(summary = "[DOC-005] Xem tất cả tài liệu của tù nhân", description = "Lấy danh sách tất cả tài liệu liên quan đến một tù nhân, được sắp xếp theo ngày tải lên")
    public ResponseEntity<List<DocumentResponse>> getPrisonerDocuments(@PathVariable UUID prisonerId) {
        List<DocumentResponse> documents = getPrisonerDocumentsUseCase.execute(prisonerId);
        return ResponseEntity.ok(documents);
    }
}
