package vn.gov.prison.secure.presentation.controller;

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

/**
 * REST Controller for Workflow Control endpoints
 */
@RestController
@RequestMapping("/api/v1/workflows")
@RequiredArgsConstructor
public class WorkflowController {

    private final InitiateWorkflowUseCase initiateWorkflowUseCase;
    private final CompleteWorkflowStepUseCase completeWorkflowStepUseCase;
    private final ApproveWorkflowUseCase approveWorkflowUseCase;
    private final GetPrisonerWorkflowsUseCase getPrisonerWorkflowsUseCase;

    /**
     * Initiate a new workflow instance
     */
    @PostMapping("/initiate")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<WorkflowInstanceResponse> initiateWorkflow(
            @Valid @RequestBody InitiateWorkflowRequest request) {
        WorkflowInstanceResponse response = initiateWorkflowUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Complete a workflow step
     */
    @PostMapping("/instances/{instanceId}/complete-step")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<WorkflowInstanceResponse> completeStep(
            @PathVariable UUID instanceId) {
        WorkflowInstanceResponse response = completeWorkflowStepUseCase.execute(instanceId);
        return ResponseEntity.ok(response);
    }

    /**
     * Approve or reject a workflow
     */
    @PostMapping("/instances/{instanceId}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<WorkflowInstanceResponse> approveWorkflow(
            @PathVariable UUID instanceId,
            @Valid @RequestBody ApprovalRequest request) {
        WorkflowInstanceResponse response = approveWorkflowUseCase.execute(instanceId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all workflow instances for a prisoner
     */
    @GetMapping("/prisoners/{prisonerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<WorkflowInstanceResponse>> getPrisonerWorkflows(
            @PathVariable UUID prisonerId) {
        List<WorkflowInstanceResponse> workflows = getPrisonerWorkflowsUseCase.execute(prisonerId);
        return ResponseEntity.ok(workflows);
    }
}
