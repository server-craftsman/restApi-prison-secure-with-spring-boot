package vn.gov.prison.secure.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.gov.prison.secure.application.dto.escape.ApproveEscapeRequestRequest;
import vn.gov.prison.secure.application.dto.escape.CreateEscapeRequestRequest;
import vn.gov.prison.secure.application.dto.escape.EscapeRequestResponse;
import vn.gov.prison.secure.application.usecase.escape.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/escapes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Escape Requests - Yêu cầu Trốn tù", description = "API quản lý yêu cầu trốn tù: Tạo yêu cầu, phê duyệt 2 cấp (Quản giáo → Quản đốc)")
public class EscapeController {

    private final CreateEscapeRequestUseCase createEscapeRequestUseCase;
    private final GuardApproveEscapeUseCase guardApproveEscapeUseCase;
    private final WardenApproveEscapeUseCase wardenApproveEscapeUseCase;
    private final GetPendingEscapeRequestsUseCase getPendingEscapeRequestsUseCase;

    @PostMapping("/request")
    @PreAuthorize("hasAuthority('ESCAPE_REQUEST')")
    @Operation(summary = "[ESC-001] Tạo yêu cầu trốn tù", description = "Tù nhân tạo yêu cầu trốn tù từ tablet. " +
            "Yêu cầu cần được phê duyệt bởi Quản giáo và Quản đốc. " +
            "Workflow: PENDING → GUARD_APPROVED → WARDEN_APPROVED → EXECUTED")
    public ResponseEntity<EscapeRequestResponse> createRequest(@Valid @RequestBody CreateEscapeRequestRequest request) {
        EscapeRequestResponse response = createEscapeRequestUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyAuthority('ESCAPE_APPROVE_GUARD', 'ESCAPE_APPROVE_WARDEN')")
    @Operation(summary = "[ESC-002] Danh sách yêu cầu chờ duyệt", description = "Lấy danh sách yêu cầu trốn tù đang chờ phê duyệt. "
            +
            "Quản giáo và Quản đốc sử dụng để xem yêu cầu cần xử lý.")
    public ResponseEntity<List<EscapeRequestResponse>> getPendingRequests() {
        List<EscapeRequestResponse> responses = getPendingEscapeRequestsUseCase.execute();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{requestId}/guard-approve")
    @PreAuthorize("hasAuthority('ESCAPE_APPROVE_GUARD')")
    @Operation(summary = "[ESC-003] Quản giáo phê duyệt/từ chối", description = "Quản giáo phê duyệt hoặc từ chối yêu cầu trốn tù (cấp 1). "
            +
            "Nếu phê duyệt, yêu cầu chuyển sang chờ Quản đốc duyệt. " +
            "Nếu từ chối, yêu cầu kết thúc.")
    public ResponseEntity<EscapeRequestResponse> guardApprove(
            @PathVariable UUID requestId,
            @Valid @RequestBody ApproveEscapeRequestRequest request,
            Authentication authentication) {
        // Get guard ID from authentication
        UUID guardId = UUID.fromString(authentication.getName()); // Simplified
        EscapeRequestResponse response = guardApproveEscapeUseCase.execute(requestId, guardId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{requestId}/warden-approve")
    @PreAuthorize("hasAuthority('ESCAPE_APPROVE_WARDEN')")
    @Operation(summary = "[ESC-004] Quản đốc phê duyệt/từ chối", description = "Quản đốc phê duyệt hoặc từ chối yêu cầu trốn tù (cấp 2 - cuối cùng). "
            +
            "Chỉ có thể duyệt yêu cầu đã được Quản giáo phê duyệt. " +
            "Nếu phê duyệt, yêu cầu sẵn sàng thực hiện.")
    public ResponseEntity<EscapeRequestResponse> wardenApprove(
            @PathVariable UUID requestId,
            @Valid @RequestBody ApproveEscapeRequestRequest request,
            Authentication authentication) {
        // Get warden ID from authentication
        UUID wardenId = UUID.fromString(authentication.getName()); // Simplified
        EscapeRequestResponse response = wardenApproveEscapeUseCase.execute(requestId, wardenId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/prisoner/{prisonerId}")
    @PreAuthorize("hasAnyAuthority('ESCAPE_REQUEST', 'ESCAPE_APPROVE_GUARD', 'ESCAPE_APPROVE_WARDEN')")
    @Operation(summary = "[ESC-005] Lịch sử yêu cầu của tù nhân", description = "Xem tất cả yêu cầu trốn tù của một tù nhân: đang chờ, đã duyệt, bị từ chối")
    public ResponseEntity<?> getPrisonerRequests(@PathVariable UUID prisonerId) {
        // TODO: Implement GetPrisonerEscapeRequestsUseCase
        return ResponseEntity.ok().build();
    }
}
