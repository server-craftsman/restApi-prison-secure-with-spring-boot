package vn.gov.prison.secure.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.gov.prison.secure.application.dto.medical.CreateMedicalRecordRequest;
import vn.gov.prison.secure.application.dto.medical.CreatePrescriptionRequest;
import vn.gov.prison.secure.application.dto.medical.MedicalRecordResponse;
import vn.gov.prison.secure.application.dto.medical.PrescriptionResponse;
import vn.gov.prison.secure.application.dto.medical.UpdateMedicalRecordRequest;
import vn.gov.prison.secure.application.usecase.medical.*;

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
    private final UpdateMedicalRecordUseCase updateMedicalRecordUseCase;
    private final GetMedicalRecordByIdUseCase getMedicalRecordByIdUseCase;
    private final DeleteMedicalRecordUseCase deleteMedicalRecordUseCase;
    private final GetPrisonerMedicalHistoryUseCase getPrisonerMedicalHistoryUseCase;
    private final CreatePrescriptionUseCase createPrescriptionUseCase;
    private final GetPrescriptionsByPrisonerUseCase getPrescriptionsByPrisonerUseCase;

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
     * Update an existing medical record
     */
    @PutMapping("/records/{recordId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICAL_STAFF')")
    public ResponseEntity<MedicalRecordResponse> updateMedicalRecord(
            @PathVariable UUID recordId,
            @Valid @RequestBody UpdateMedicalRecordRequest request) {
        MedicalRecordResponse response = updateMedicalRecordUseCase.execute(recordId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get medical record by ID
     */
    @GetMapping("/records/{recordId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICAL_STAFF', 'STAFF')")
    public ResponseEntity<MedicalRecordResponse> getMedicalRecord(@PathVariable UUID recordId) {
        MedicalRecordResponse response = getMedicalRecordByIdUseCase.execute(recordId);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a medical record
     */
    @DeleteMapping("/records/{recordId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICAL_STAFF')")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable UUID recordId) {
        deleteMedicalRecordUseCase.execute(recordId);
        return ResponseEntity.noContent().build();
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

    /**
     * Create a new prescription
     */
    @PostMapping("/prescriptions")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICAL_STAFF')")
    public ResponseEntity<PrescriptionResponse> createPrescription(
            @Valid @RequestBody CreatePrescriptionRequest request) {
        PrescriptionResponse response = createPrescriptionUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all prescriptions for a prisoner
     */
    @GetMapping("/prisoners/{prisonerId}/prescriptions")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICAL_STAFF', 'STAFF')")
    public ResponseEntity<List<PrescriptionResponse>> getPrisonerPrescriptions(
            @PathVariable UUID prisonerId) {
        List<PrescriptionResponse> prescriptions = getPrescriptionsByPrisonerUseCase.execute(prisonerId);
        return ResponseEntity.ok(prescriptions);
    }
}
