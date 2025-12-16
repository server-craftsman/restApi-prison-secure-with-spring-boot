package vn.gov.prison.secure.presentation.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.gov.prison.secure.application.dto.request.RegisterPrisonerRequest;
import vn.gov.prison.secure.application.dto.response.PrisonerResponse;
import vn.gov.prison.secure.application.usecase.prisoner.RegisterPrisonerUseCase;

import java.util.List;

/**
 * REST Controller for Prisoner Management
 * Following Controller Pattern and SRP
 * 
 * Handles HTTP requests and delegates to use cases
 */
@RestController
@RequestMapping("/api/v1/prisoners")
@CrossOrigin(origins = "*")
public class PrisonerController {

    private final RegisterPrisonerUseCase registerPrisonerUseCase;
    // Other use cases would be injected here

    public PrisonerController(RegisterPrisonerUseCase registerPrisonerUseCase) {
        this.registerPrisonerUseCase = registerPrisonerUseCase;
    }

    /**
     * POST /api/v1/prisoners - Register new prisoner
     */
    @PostMapping
    @PreAuthorize("hasAuthority('PRISONER_CREATE')")
    public ResponseEntity<PrisonerResponse> registerPrisoner(
            @Valid @RequestBody RegisterPrisonerRequest request) {

        PrisonerResponse response = registerPrisonerUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/v1/prisoners/{id} - Get prisoner by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PRISONER_READ')")
    public ResponseEntity<PrisonerResponse> getPrisonerById(@PathVariable String id) {
        // This would use a GetPrisonerByIdUseCase
        // For now, return 404
        return ResponseEntity.notFound().build();
    }

    /**
     * GET /api/v1/prisoners - Search prisoners
     */
    @GetMapping
    @PreAuthorize("hasAuthority('PRISONER_READ')")
    public ResponseEntity<List<PrisonerResponse>> searchPrisoners(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String facility,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        // This would use a SearchPrisonersUseCase
        return ResponseEntity.ok(List.of());
    }

    /**
     * PUT /api/v1/prisoners/{id} - Update prisoner info
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PRISONER_UPDATE')")
    public ResponseEntity<PrisonerResponse> updatePrisoner(
            @PathVariable String id,
            @Valid @RequestBody RegisterPrisonerRequest request) {

        // This would use an UpdatePrisonerUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/v1/prisoners/{id}/biometric - Capture biometric data
     */
    @PostMapping("/{id}/biometric")
    @PreAuthorize("hasAuthority('BIOMETRIC_CAPTURE')")
    public ResponseEntity<Void> captureBiometric(
            @PathVariable String id,
            @Valid @RequestBody vn.gov.prison.secure.application.dto.request.BiometricCaptureRequest request) {

        // This would use a CaptureBiometricUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/v1/prisoners/{id}/verify - Verify prisoner identity
     */
    @PostMapping("/{id}/verify")
    @PreAuthorize("hasAuthority('BIOMETRIC_VERIFY')")
    public ResponseEntity<Void> verifyIdentity(
            @PathVariable String id,
            @Valid @RequestBody vn.gov.prison.secure.application.dto.request.BiometricCaptureRequest request) {

        // This would use a VerifyPrisonerIdentityUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/v1/prisoners/{id}/release - Release prisoner
     */
    @PostMapping("/{id}/release")
    @PreAuthorize("hasAuthority('PRISONER_RELEASE')")
    public ResponseEntity<Void> releasePrisoner(
            @PathVariable String id,
            @RequestParam String reason) {

        // This would use a ReleasePrisonerUseCase
        return ResponseEntity.ok().build();
    }
}
