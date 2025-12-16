package vn.gov.prison.secure.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.gov.prison.secure.application.dto.medical.CreateMedicalRecordRequest;
import vn.gov.prison.secure.application.dto.medical.MedicalRecordResponse;
import vn.gov.prison.secure.application.usecase.medical.CreateMedicalRecordUseCase;
import vn.gov.prison.secure.application.usecase.medical.GetPrisonerMedicalHistoryUseCase;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Medical Management endpoints
 */
@RestController
@RequestMapping("/api/v1/medical")
@RequiredArgsConstructor
public class MedicalController {

    private final CreateMedicalRecordUseCase createMedicalRecordUseCase;
    private final GetPrisonerMedicalHistoryUseCase getPrisonerMedicalHistoryUseCase;

    /**
     * Create a new medical record
     */
    @PostMapping("/records")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICAL_STAFF')")
    public ResponseEntity<MedicalRecordResponse> createMedicalRecord(
            @Valid @RequestBody CreateMedicalRecordRequest request) {
        MedicalRecordResponse response = createMedicalRecordUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get medical history for a prisoner
     */
    @GetMapping("/prisoners/{prisonerId}/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICAL_STAFF', 'STAFF')")
    public ResponseEntity<List<MedicalRecordResponse>> getPrisonerMedicalHistory(
            @PathVariable UUID prisonerId) {
        List<MedicalRecordResponse> history = getPrisonerMedicalHistoryUseCase.execute(prisonerId);
        return ResponseEntity.ok(history);
    }
}
