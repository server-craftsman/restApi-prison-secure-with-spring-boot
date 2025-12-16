package vn.gov.prison.secure.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.gov.prison.secure.application.dto.scheduling.CreateScheduleRequest;
import vn.gov.prison.secure.application.dto.scheduling.ScheduleResponse;
import vn.gov.prison.secure.application.dto.scheduling.UpdateScheduleRequest;
import vn.gov.prison.secure.application.usecase.scheduling.CancelScheduleUseCase;
import vn.gov.prison.secure.application.usecase.scheduling.CreateScheduleUseCase;
import vn.gov.prison.secure.application.usecase.scheduling.GetPrisonerScheduleUseCase;
import vn.gov.prison.secure.application.usecase.scheduling.UpdateScheduleUseCase;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Scheduling Management endpoints
 */
@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class SchedulingController {

    private final CreateScheduleUseCase createScheduleUseCase;
    private final UpdateScheduleUseCase updateScheduleUseCase;
    private final GetPrisonerScheduleUseCase getPrisonerScheduleUseCase;
    private final CancelScheduleUseCase cancelScheduleUseCase;

    /**
     * Create a new schedule
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ScheduleResponse> createSchedule(
            @Valid @RequestBody CreateScheduleRequest request) {
        ScheduleResponse response = createScheduleUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update an existing schedule
     */
    @PutMapping("/{scheduleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ScheduleResponse> updateSchedule(
            @PathVariable UUID scheduleId,
            @Valid @RequestBody UpdateScheduleRequest request) {
        ScheduleResponse response = updateScheduleUseCase.execute(scheduleId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all schedules for a prisoner
     */
    @GetMapping("/prisoners/{prisonerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<ScheduleResponse>> getPrisonerSchedules(
            @PathVariable UUID prisonerId) {
        List<ScheduleResponse> schedules = getPrisonerScheduleUseCase.execute(prisonerId);
        return ResponseEntity.ok(schedules);
    }

    /**
     * Cancel a schedule
     */
    @DeleteMapping("/{scheduleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Void> cancelSchedule(@PathVariable UUID scheduleId) {
        cancelScheduleUseCase.execute(scheduleId);
        return ResponseEntity.noContent().build();
    }
}
