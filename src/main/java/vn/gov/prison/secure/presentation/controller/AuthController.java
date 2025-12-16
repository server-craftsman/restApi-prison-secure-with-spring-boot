package vn.gov.prison.secure.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.gov.prison.secure.application.dto.auth.BiometricLoginRequest;
import vn.gov.prison.secure.application.dto.auth.LoginRequest;
import vn.gov.prison.secure.application.dto.auth.LoginResponse;
import vn.gov.prison.secure.application.usecase.auth.LoginWithBiometricUseCase;
import vn.gov.prison.secure.application.usecase.auth.LoginWithPasswordUseCase;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Authentication - Xác thực", description = "API xác thực người dùng: Đăng nhập bằng mật khẩu hoặc vân tay")
public class AuthController {

    private final LoginWithPasswordUseCase loginWithPasswordUseCase;
    private final LoginWithBiometricUseCase loginWithBiometricUseCase;

    @PostMapping("/login")
    @Operation(summary = "[AUTH-001] Đăng nhập bằng username/password", description = """
            **🔓 Public endpoint - No authentication required**

            Đăng nhập cho Quản đốc và Quản giáo sử dụng username và password.
            Trả về JWT token với thời hạn 24 giờ.

            **Roles**: WARDEN, GUARD
            """)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = loginWithPasswordUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/biometric")
    @Operation(summary = "[AUTH-002] Đăng nhập bằng vân tay", description = """
            **🔓 Public endpoint - No authentication required**

            Đăng nhập cho Tù nhân sử dụng dữ liệu vân tay từ tablet.
            Trả về JWT token với thời hạn 8 giờ.

            **Role**: PRISONER
            """)
    public ResponseEntity<LoginResponse> biometricLogin(@Valid @RequestBody BiometricLoginRequest request) {
        LoginResponse response = loginWithBiometricUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "[AUTH-003] Đăng xuất", description = """
            **🔒 Requires authentication**

            Đăng xuất khỏi hệ thống. Client cần xóa token đã lưu.
            JWT is stateless, client should delete the token.

            **Roles**: ALL
            """)
    public ResponseEntity<Void> logout() {
        // JWT is stateless, client should delete the token
        return ResponseEntity.ok().build();
    }
}
