package vn.gov.prison.secure.application.usecase.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.auth.LoginRequest;
import vn.gov.prison.secure.application.dto.auth.LoginResponse;
import vn.gov.prison.secure.domain.user.User;
import vn.gov.prison.secure.domain.user.UserRepository;
import vn.gov.prison.secure.infrastructure.security.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class LoginWithPasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public LoginResponse execute(LoginRequest request) {
        // Find user by username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Check if user is active
        if (!user.isActive()) {
            throw new RuntimeException("User account is not active");
        }

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user);
        Long expiresIn = jwtTokenProvider.getExpirationTime();

        // Build user info
        LoginResponse.UserInfo userInfo = buildUserInfo(user);

        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .user(userInfo)
                .build();
    }

    private LoginResponse.UserInfo buildUserInfo(User user) {
        return LoginResponse.UserInfo.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .userType(user.getUserType().name())
                .email(user.getEmail())
                .build();
    }
}
