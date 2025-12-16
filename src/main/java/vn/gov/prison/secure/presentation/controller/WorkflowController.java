package vn.gov.prison.secure.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.gov.prison.secure.application.dto.workflow.ApprovalRequest;
import vn.gov.prison.secure.application.dto.workflow.InitiateWorkflowRequest;
import vn.gov.prison.secure.application.dto.workflow.WorkflowInstanceResponse;
import vn.gov.prison.secure.application.usecase.workflow.ApproveWorkflowUseCase;
import vn.gov.prison.secure.application.usecase.workflow.CompleteWorkflowStepUseCase;
import vn.gov.prison.secure.application.usecase.workflow.GetPrisonerWorkflowsUseCase;
import vn.gov.prison.secure.application.usecase.workflow.InitiateWorkflowUseCase;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workflows")
@RequiredArgsConstructor
@Tag(name = "Workflow Control - Quản lý Quy trình", description = "API quản lý quy trình nghiệp vụ, phê duyệt và theo dõi tiến độ")
public class WorkflowController {

    private final InitiateWorkflowUseCase initiateWorkflowUseCase;
    private final CompleteWorkflowStepUseCase completeWorkflowStepUseCase;
    private final ApproveWorkflowUseCase approveWorkflowUseCase;
    private final GetPrisonerWorkflowsUseCase getPrisonerWorkflowsUseCase;

    @PostMapping("/initiate")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(summary = "[WF-001] Khởi tạo quy trình mới", description = "Khởi tạo một quy trình nghiệp vụ mới cho tù nhân. "
            +
            "Quy trình có thể là: chuyển trại, thả tù, điều trị đặc biệt, v.v. " +
            "Hệ thống tự động tạo các bước cần thực hiện.")
    public ResponseEntity<WorkflowInstanceResponse> initiateWorkflow(
            @Valid @RequestBody InitiateWorkflowRequest request) {
        WorkflowInstanceResponse response = initiateWorkflowUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/instances/{instanceId}/complete-step")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(summary = "[WF-002] Hoàn thành bước quy trình", description = "Đánh dấu hoàn thành một bước trong quy trình. "
            +
            "Hệ thống tự động chuyển sang bước tiếp theo và thông báo cho người phụ trách.")
    public ResponseEntity<WorkflowInstanceResponse> completeStep(@PathVariable UUID instanceId) {
        WorkflowInstanceResponse response = completeWorkflowStepUseCase.execute(instanceId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/instances/{instanceId}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "[WF-003] Phê duyệt/từ chối quy trình", description = "Phê duyệt hoặc từ chối quy trình. Nếu từ chối cần ghi rõ lý do. "
            +
            "Quy trình được phê duyệt sẽ tiếp tục, quy trình bị từ chối sẽ dừng lại.")
    public ResponseEntity<WorkflowInstanceResponse> approveWorkflow(
            @PathVariable UUID instanceId,
            @Valid @RequestBody ApprovalRequest request) {
        WorkflowInstanceResponse response = approveWorkflowUseCase.execute(instanceId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/prisoners/{prisonerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(summary = "[WF-004] Xem quy trình của tù nhân", description = "Lấy tất cả quy trình liên quan đến tù nhân bao gồm: đang chờ, đang thực hiện, đã hoàn thành, bị từ chối")
    public ResponseEntity<List<WorkflowInstanceResponse>> getPrisonerWorkflows(@PathVariable UUID prisonerId) {
        List<WorkflowInstanceResponse> workflows = getPrisonerWorkflowsUseCase.execute(prisonerId);
        return ResponseEntity.ok(workflows);
    }
}
