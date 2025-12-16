package vn.gov.prison.secure.domain.event;

import vn.gov.prison.secure.domain.model.prisoner.BiometricData;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain Event: Biometric Verification Failed
 * Published when biometric verification fails
 */
public record BiometricVerificationFailedEvent(
        String eventId,
        LocalDateTime occurredAt,
        String prisonerId,
        BiometricData.BiometricType biometricType,
        double confidenceScore,
        String reason) implements DomainEvent {

    public BiometricVerificationFailedEvent(String prisonerId,
            BiometricData.BiometricType biometricType,
            double confidenceScore,
            String reason) {
        this(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                prisonerId,
                biometricType,
                confidenceScore,
                reason);
    }

    @Override
    public String eventType() {
        return "BiometricVerificationFailed";
    }

    @Override
    public String aggregateId() {
        return prisonerId;
    }
}
