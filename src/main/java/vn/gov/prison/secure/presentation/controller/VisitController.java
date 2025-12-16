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
@Tag(name = "Visitor Management", description = "APIs for managing prisoner visits")
public class VisitController {

    private final CreateVisitRequestUseCase createVisitRequestUseCase;
    private final ApproveVisitRequestUseCase approveVisitRequestUseCase;
    private final LogVisitUseCase logVisitUseCase;
    private final GetPrisonerVisitsUseCase getPrisonerVisitsUseCase;

    @PostMapping("/requests")
    @Operation(summary = "Create visit request")
    public ResponseEntity<VisitRequestResponse> createVisitRequest(@RequestBody CreateVisitRequestRequest request) {
        VisitRequestResponse response = createVisitRequestUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/requests/{id}/approve")
    @Operation(summary = "Approve or reject visit request")
    public ResponseEntity<VisitRequestResponse> approveVisitRequest(
            @PathVariable UUID id,
            @RequestBody ApproveVisitRequest request) {
        VisitRequestResponse response = approveVisitRequestUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/log")
    @Operation(summary = "Log visit completion")
    public ResponseEntity<VisitRequestResponse> logVisit(
            @PathVariable UUID id,
            @RequestBody VisitLogRequest request) {
        VisitRequestResponse response = logVisitUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/prisoners/{prisonerId}")
    @Operation(summary = "Get prisoner visit history")
    public ResponseEntity<List<VisitRequestResponse>> getPrisonerVisits(@PathVariable UUID prisonerId) {
        List<VisitRequestResponse> visits = getPrisonerVisitsUseCase.execute(prisonerId);
        return ResponseEntity.ok(visits);
    }
}
