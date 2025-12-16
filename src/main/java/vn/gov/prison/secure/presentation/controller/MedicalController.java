package vn.gov.prison.secure.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.gov.prison.secure.application.dto.medical.*;
import vn.gov.prison.secure.application.usecase.medical.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/medical")
@RequiredArgsConstructor
@Tag(name = "Medical Management - Quản lý Y tế", description = "API quản lý hồ sơ y tế, đơn thuốc và khám sức khỏe cho tù nhân")
public class MedicalController {

    private final CreateMedicalRecordUseCase createMedicalRecordUseCase;
    private final UpdateMedicalRecordUseCase updateMedicalRecordUseCase;
    private final GetMedicalRecordByIdUseCase getMedicalRecordByIdUseCase;
    private final DeleteMedicalRecordUseCase deleteMedicalRecordUseCase;
    private final GetPrisonerMedicalHistoryUseCase getPrisonerMedicalHistoryUseCase;
    private final CreatePrescriptionUseCase createPrescriptionUseCase;
    private final GetPrescriptionsByPrisonerUseCase getPrescriptionsByPrisonerUseCase;

    @PostMapping("/records")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICAL_STAFF')")
    @Operation(summary = "[MED-001] Tạo hồ sơ y tế mới", description = "Tạo hồ sơ y tế mới cho tù nhân bao gồm chẩn đoán, điều trị và mức độ nghiêm trọng. Hồ sơ sẽ được lưu vào lịch sử y tế của tù nhân.")
    public ResponseEntity<MedicalRecordResponse> createMedicalRecord(
            @Valid @RequestBody CreateMedicalRecordRequest request) {
        MedicalRecordResponse response = createMedicalRecordUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/records/{recordId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICAL_STAFF')")
    @Operation(summary = "[MED-002] Cập nhật hồ sơ y tế", description = "Cập nhật thông tin hồ sơ y tế hiện có bao gồm chẩn đoán, điều trị, ghi chú và lịch tái khám")
    public ResponseEntity<MedicalRecordResponse> updateMedicalRecord(
            @PathVariable UUID recordId,
            @Valid @RequestBody UpdateMedicalRecordRequest request) {
        MedicalRecordResponse response = updateMedicalRecordUseCase.execute(recordId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/records/{recordId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICAL_STAFF', 'STAFF')")
    @Operation(summary = "[MED-003] Xem chi tiết hồ sơ y tế", description = "Lấy thông tin chi tiết của một hồ sơ y tế theo ID")
    public ResponseEntity<MedicalRecordResponse> getMedicalRecord(@PathVariable UUID recordId) {
        MedicalRecordResponse response = getMedicalRecordByIdUseCase.execute(recordId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/records/{recordId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICAL_STAFF')")
    @Operation(summary = "[MED-004] Xóa hồ sơ y tế", description = "Xóa hồ sơ y tế khỏi hệ thống (chỉ dành cho admin)")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable UUID recordId) {
        deleteMedicalRecordUseCase.execute(recordId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/prisoners/{prisonerId}/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICAL_STAFF', 'STAFF')")
    @Operation(summary = "[MED-005] Xem lịch sử y tế tù nhân", description = "Lấy toàn bộ lịch sử y tế của tù nhân bao gồm tất cả hồ sơ khám bệnh, điều trị")
    public ResponseEntity<List<MedicalRecordResponse>> getPrisonerMedicalHistory(@PathVariable UUID prisonerId) {
        List<MedicalRecordResponse> history = getPrisonerMedicalHistoryUseCase.execute(prisonerId);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/prescriptions")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICAL_STAFF')")
    @Operation(summary = "[MED-006] Kê đơn thuốc", description = "Tạo đơn thuốc mới cho tù nhân bao gồm tên thuốc, liều lượng, tần suất và thời gian sử dụng")
    public ResponseEntity<PrescriptionResponse> createPrescription(
            @Valid @RequestBody CreatePrescriptionRequest request) {
        PrescriptionResponse response = createPrescriptionUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/prisoners/{prisonerId}/prescriptions")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICAL_STAFF', 'STAFF')")
    @Operation(summary = "[MED-007] Xem danh sách đơn thuốc", description = "Lấy tất cả đơn thuốc hiện tại và lịch sử đơn thuốc của tù nhân")
    public ResponseEntity<List<PrescriptionResponse>> getPrescriptions(@PathVariable UUID prisonerId) {
        List<PrescriptionResponse> prescriptions = getPrescriptionsByPrisonerUseCase.execute(prisonerId);
        return ResponseEntity.ok(prescriptions);
    }
}
