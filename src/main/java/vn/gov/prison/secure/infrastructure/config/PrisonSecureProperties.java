package vn.gov.prison.secure.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ============================================
 * Cấu hình ứng dụng Prison Secure
 * 
 * Sử dụng @ConfigurationProperties để mapping từ file properties
 * Cách dùng: @Autowired private PrisonSecureProperties props;
 * ============================================
 */
@Component
@ConfigurationProperties(prefix = "prison-secure")
@Getter
@Setter
public class PrisonSecureProperties {

    // ============================================
    // Biometric Configuration
    // ============================================
    private BiometricProperties biometric = new BiometricProperties();

    // ============================================
    // Security Configuration
    // ============================================
    private SecurityProperties security = new SecurityProperties();

    // ============================================
    // Biometric Nested Class
    // ============================================
    @Getter
    @Setter
    public static class BiometricProperties {
        /**
         * Ngưỡng xác minh sinh trắc học (0-1)
         * Ví dụ: 0.95 = 95% trùng khớp
         */
        private double verificationThreshold = 0.95;

        /**
         * Ngưỡng tìm kiếm sinh trắc học (0-1)
         * Ví dụ: 0.80 = 80% trùng khớp
         */
        private double searchThreshold = 0.80;
    }

    // ============================================
    // Security Nested Class
    // ============================================
    @Getter
    @Setter
    public static class SecurityProperties {
        private JwtProperties jwt = new JwtProperties();
    }

    // ============================================
    // JWT Configuration Nested Class
    // ============================================
    @Getter
    @Setter
    public static class JwtProperties {
        /**
         * JWT Secret Key (256-bit)
         * Lấy từ environment variable: JWT_SECRET
         */
        private String secret = "change-this-secret-in-production";

        /**
         * JWT Expiration time (milliseconds)
         * Default: 86400000 (24 hours)
         */
        private long expiration = 86400000;

        /**
         * Prisoner JWT Expiration time (milliseconds)
         * Default: 28800000 (8 hours)
         * Prisoners có session ngắn hơn nhân viên
         */
        private long prisonerExpiration = 28800000;

        /**
         * JWT Issuer
         */
        private String issuer = "prison-secure-system";

        /**
         * JWT Audience
         */
        private String audience = "prison-secure-users";

        /**
         * Refresh token expiration (milliseconds)
         * Default: 604800000 (7 days)
         */
        private long refreshTokenExpiration = 604800000;

        public boolean isTokenExpired(long tokenCreatedTime) {
            return System.currentTimeMillis() - tokenCreatedTime > expiration;
        }

        public boolean isPrisonerTokenExpired(long tokenCreatedTime) {
            return System.currentTimeMillis() - tokenCreatedTime > prisonerExpiration;
        }
    }
}
