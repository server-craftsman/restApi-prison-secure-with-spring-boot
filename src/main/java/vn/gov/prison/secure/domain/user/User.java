package vn.gov.prison.secure.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class User {
    private UserId id;
    private String username;
    private String passwordHash;
    private String email;
    private String fullName;
    private UserType userType;
    private UserStatus status;
    private String fingerprintData; // For biometric authentication
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Factory method for creating new user
    public static User create(
            String username,
            String passwordHash,
            String fullName,
            UserType userType,
            String email) {
        return User.builder()
                .id(UserId.generate())
                .username(username)
                .passwordHash(passwordHash)
                .fullName(fullName)
                .userType(userType)
                .email(email)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // Factory method for prisoner with biometric
    public static User createPrisoner(
            String username,
            String passwordHash,
            String fullName,
            String fingerprintData) {
        return User.builder()
                .id(UserId.generate())
                .username(username)
                .passwordHash(passwordHash)
                .fullName(fullName)
                .userType(UserType.PRISONER)
                .fingerprintData(fingerprintData)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // Business methods
    public boolean isPrisoner() {
        return userType == UserType.PRISONER;
    }

    public boolean isGuard() {
        return userType == UserType.GUARD;
    }

    public boolean isWarden() {
        return userType == UserType.WARDEN;
    }

    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }

    public void suspend() {
        this.status = UserStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateFingerprintData(String fingerprintData) {
        if (!isPrisoner()) {
            throw new IllegalStateException("Only prisoners can have fingerprint data");
        }
        this.fingerprintData = fingerprintData;
        this.updatedAt = LocalDateTime.now();
    }
}
