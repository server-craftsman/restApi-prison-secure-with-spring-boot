package vn.gov.prison.secure.domain.service;

import vn.gov.prison.secure.domain.model.prisoner.BiometricData;

/**
 * Domain Service for biometric verification
 * Following DIP - interface defined in domain, implemented in infrastructure
 * 
 * This service coordinates with external biometric systems
 * (fingerprint scanners, iris scanners, facial recognition, etc.)
 */
public interface BiometricVerificationService {

    /**
     * Verify biometric data against stored template
     * 
     * @param capturedData Newly captured biometric data
     * @param storedData   Stored biometric template
     * @return Verification result with confidence score
     */
    VerificationResult verify(BiometricData capturedData, BiometricData storedData);

    /**
     * Calculate match score between two biometric samples
     * 
     * @param sample1 First biometric sample
     * @param sample2 Second biometric sample
     * @return Match score (0.0 to 1.0)
     */
    double calculateMatchScore(BiometricData sample1, BiometricData sample2);

    /**
     * Verify specific biometric type
     */
    VerificationResult verifyByType(BiometricData.BiometricType type,
            BiometricData capturedData,
            BiometricData storedData);

    /**
     * Verification Result
     */
    record VerificationResult(
            boolean matched,
            double confidenceScore,
            BiometricData.BiometricType type,
            String details) {
        public boolean isHighConfidence() {
            return confidenceScore >= 0.95;
        }

        public boolean isMediumConfidence() {
            return confidenceScore >= 0.80 && confidenceScore < 0.95;
        }

        public boolean isLowConfidence() {
            return confidenceScore < 0.80;
        }
    }
}
