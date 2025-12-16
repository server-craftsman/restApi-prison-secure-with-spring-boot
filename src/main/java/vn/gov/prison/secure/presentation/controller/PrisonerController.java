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
import vn.gov.prison.secure.application.dto.request.BiometricCaptureRequest;
import vn.gov.prison.secure.application.dto.request.RegisterPrisonerRequest;
import vn.gov.prison.secure.application.dto.response.PrisonerResponse;
import vn.gov.prison.secure.application.usecase.prisoner.RegisterPrisonerUseCase;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prisoners")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Prisoner Management - Quản lý Tù nhân", description = "API quản lý thông tin tù nhân, đăng ký, cập nhật và sinh trắc học")
public class PrisonerController {

    private final RegisterPrisonerUseCase registerPrisonerUseCase;

    @PostMapping
    @PreAuthorize("hasAuthority('PRISONER_CREATE')")
    @Operation(summary = "[PRIS-001] Đăng ký tù nhân mới", description = "Đăng ký tù nhân mới vào hệ thống bao gồm thông tin cá nhân, nhân dạng, địa chỉ. "
            +
            "Tự động tạo mã tù nhân và khởi tạo hồ sơ cơ bản.")
    public ResponseEntity<PrisonerResponse> registerPrisoner(@Valid @RequestBody RegisterPrisonerRequest request) {
        PrisonerResponse response = registerPrisonerUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PRISONER_READ')")
    @Operation(summary = "[PRIS-002] Xem thông tin tù nhân", description = "Lấy thông tin chi tiết của tù nhân theo ID bao gồm thông tin cá nhân, trạng thái, lịch sử")
    public ResponseEntity<PrisonerResponse> getPrisonerById(@PathVariable String id) {
        // This would use a GetPrisonerByIdUseCase
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PRISONER_READ')")
    @Operation(summary = "[PRIS-003] Tìm kiếm tù nhân", description = "Tìm kiếm tù nhân theo nhiều tiêu chí: trạng thái, cơ sở giam giữ, họ tên. "
            +
            "Hỗ trợ phân trang để xử lý danh sách lớn.")
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

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PRISONER_UPDATE')")
    @Operation(summary = "[PRIS-004] Cập nhật thông tin tù nhân", description = "Cập nhật thông tin cá nhân, địa chỉ, liên hệ khẩn cấp của tù nhân")
    public ResponseEntity<PrisonerResponse> updatePrisoner(
            @PathVariable String id,
            @Valid @RequestBody RegisterPrisonerRequest request) {
        // This would use an UpdatePrisonerUseCase
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/biometric")
    @PreAuthorize("hasAuthority('BIOMETRIC_CAPTURE')")
    @Operation(summary = "[PRIS-005] Thu thập dữ liệu sinh trắc học", description = "Thu thập và lưu trữ dữ liệu sinh trắc học của tù nhân: vân tay, ảnh chân dung, mống mắt. "
            +
            "Dữ liệu được mã hóa và lưu trữ an toàn.")
    public ResponseEntity<Void> captureBiometric(
            @PathVariable String id,
            @Valid @RequestBody BiometricCaptureRequest request) {
        // This would use a CaptureBiometricUseCase
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/verify")
    @PreAuthorize("hasAuthority('BIOMETRIC_VERIFY')")
    @Operation(summary = "[PRIS-006] Xác thực danh tính tù nhân", description = "Xác thực danh tính tù nhân bằng dữ liệu sinh trắc học. "
            +
            "So sánh dữ liệu hiện tại với dữ liệu đã lưu trong hệ thống.")
    public ResponseEntity<Void> verifyIdentity(
            @PathVariable String id,
            @Valid @RequestBody BiometricCaptureRequest request) {
        // This would use a VerifyPrisonerIdentityUseCase
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/release")
    @PreAuthorize("hasAuthority('PRISONER_RELEASE')")
    @Operation(summary = "[PRIS-007] Thả tù nhân", description = "Xử lý thả tù nhân khỏi hệ thống. Cập nhật trạng thái, ghi nhận lý do thả. "
            +
            "Tự động kích hoạt quy trình hoàn trả tài sản và cập nhật hồ sơ.")
    public ResponseEntity<Void> releasePrisoner(
            @PathVariable String id,
            @RequestParam String reason) {
        // This would use a ReleasePrisonerUseCase
        return ResponseEntity.ok().build();
    }
}
