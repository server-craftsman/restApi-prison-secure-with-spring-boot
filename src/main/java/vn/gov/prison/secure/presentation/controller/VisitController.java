package vn.gov.prison.secure.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.gov.prison.secure.application.dto.visitor.ApproveVisitRequest;
import vn.gov.prison.secure.application.dto.visitor.CreateVisitRequestRequest;
import vn.gov.prison.secure.application.dto.visitor.VisitLogRequest;
import vn.gov.prison.secure.application.dto.visitor.VisitRequestResponse;
import vn.gov.prison.secure.application.usecase.visitor.ApproveVisitRequestUseCase;
import vn.gov.prison.secure.application.usecase.visitor.CreateVisitRequestUseCase;
import vn.gov.prison.secure.application.usecase.visitor.GetPrisonerVisitsUseCase;
import vn.gov.prison.secure.application.usecase.visitor.LogVisitUseCase;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/visits")
@RequiredArgsConstructor
@Tag(name = "Visitor Management - Quản lý Thăm nuôi", description = "API quản lý yêu cầu thăm nuôi, phê duyệt và ghi nhận buổi thăm")
public class VisitController {

    private final CreateVisitRequestUseCase createVisitRequestUseCase;
    private final ApproveVisitRequestUseCase approveVisitRequestUseCase;
    private final LogVisitUseCase logVisitUseCase;
    private final GetPrisonerVisitsUseCase getPrisonerVisitsUseCase;

    @PostMapping("/requests")
    @Operation(summary = "[VISIT-001] Tạo yêu cầu thăm nuôi", description = "Tạo yêu cầu thăm nuôi mới cho tù nhân bao gồm thông tin người thăm, thời gian, mục đích. "
            +
            "Yêu cầu sẽ ở trạng thái PENDING và chờ phê duyệt từ cán bộ quản lý.")
    public ResponseEntity<VisitRequestResponse> createVisitRequest(@RequestBody CreateVisitRequestRequest request) {
        VisitRequestResponse response = createVisitRequestUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/requests/{id}/approve")
    @Operation(summary = "[VISIT-002] Phê duyệt/từ chối yêu cầu thăm nuôi", description = "Phê duyệt hoặc từ chối yêu cầu thăm nuôi. Nếu từ chối cần ghi rõ lý do. "
            +
            "Hệ thống sẽ tự động gửi thông báo cho người đăng ký.")
    public ResponseEntity<VisitRequestResponse> approveVisitRequest(
            @PathVariable UUID id,
            @RequestBody ApproveVisitRequest request) {
        VisitRequestResponse response = approveVisitRequestUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/log")
    @Operation(summary = "[VISIT-003] Ghi nhận buổi thăm nuôi đã hoàn thành", description = "Ghi nhận thông tin buổi thăm nuôi đã diễn ra bao gồm thời gian bắt đầu, kết thúc, ghi chú. "
            +
            "Dữ liệu sẽ được lưu vào lịch sử thăm nuôi.")
    public ResponseEntity<VisitRequestResponse> logVisit(
            @PathVariable UUID id,
            @RequestBody VisitLogRequest request) {
        VisitRequestResponse response = logVisitUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/prisoners/{prisonerId}")
    @Operation(summary = "[VISIT-004] Xem lịch sử thăm nuôi của tù nhân", description = "Lấy toàn bộ lịch sử thăm nuôi của tù nhân bao gồm các buổi đã hoàn thành, đang chờ và bị từ chối")
    public ResponseEntity<List<VisitRequestResponse>> getPrisonerVisits(@PathVariable UUID prisonerId) {
        List<VisitRequestResponse> visits = getPrisonerVisitsUseCase.execute(prisonerId);
        return ResponseEntity.ok(visits);
    }
}
