package vn.gov.prison.secure.application.usecase.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.auth.BiometricLoginRequest;
import vn.gov.prison.secure.application.dto.auth.LoginResponse;
import vn.gov.prison.secure.domain.user.User;
import vn.gov.prison.secure.domain.user.UserRepository;
import vn.gov.prison.secure.infrastructure.security.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class LoginWithBiometricUseCase {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public LoginResponse execute(BiometricLoginRequest request) {
        // Find user by fingerprint data
        User user = userRepository.findByFingerprintData(request.getFingerprintData())
                .orElseThrow(() -> new RuntimeException("Fingerprint not recognized"));

        // Verify user is a prisoner
        if (!user.isPrisoner()) {
            throw new RuntimeException("Biometric login is only for prisoners");
        }

        // Check if user is active
        if (!user.isActive()) {
            throw new RuntimeException("User account is not active");
        }

        // Generate JWT token (shorter expiration for prisoners)
        String token = jwtTokenProvider.generatePrisonerToken(user);
        Long expiresIn = jwtTokenProvider.getPrisonerExpirationTime();

        // Build user info
        LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .userType(user.getUserType().name())
                .build();

        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .user(userInfo)
                .build();
    }
}
