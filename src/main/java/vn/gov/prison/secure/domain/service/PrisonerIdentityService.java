package vn.gov.prison.secure.domain.service;

import vn.gov.prison.secure.domain.model.prisoner.BiometricData;
import vn.gov.prison.secure.domain.model.prisoner.Prisoner;
import vn.gov.prison.secure.domain.repository.PrisonerRepository;

/**
 * Domain Service for prisoner identity management
 * Encapsulates complex identity verification logic
 * 
 * Following SRP and DDD Domain Service pattern
 */
public class PrisonerIdentityService {

    private final BiometricVerificationService biometricVerificationService;
    private final PrisonerRepository prisonerRepository;

    public PrisonerIdentityService(BiometricVerificationService biometricVerificationService,
            PrisonerRepository prisonerRepository) {
        this.biometricVerificationService = biometricVerificationService;
        this.prisonerRepository = prisonerRepository;
    }

    /**
     * Verify prisoner identity using biometric data
     * 
     * Business rule: Identity is verified if biometric match confidence >= 95%
     */
    public IdentityVerificationResult verifyIdentity(Prisoner prisoner,
            BiometricData capturedBiometric) {
        if (prisoner == null || capturedBiometric == null) {
            throw new IllegalArgumentException("Prisoner and biometric data are required");
        }

        // Get latest stored biometric
        var storedBiometric = prisoner.getLatestBiometricData()
                .orElseThrow(() -> new IllegalStateException(
                        "No biometric data on file for prisoner: " + prisoner.getPrisonerNumber()));

        // Verify using biometric service
        var verificationResult = biometricVerificationService.verify(
                capturedBiometric, storedBiometric);

        return new IdentityVerificationResult(
                prisoner.getId(),
                verificationResult.matched(),
                verificationResult.confidenceScore(),
                verificationResult.type(),
                verificationResult.isHighConfidence());
    }

    /**
     * Search for potential matches using biometric data
     * Used for identifying unknown individuals
     */
    public java.util.List<PotentialMatch> searchByBiometric(BiometricData biometricData) {
        // Get all active prisoners
        var activePrisoners = prisonerRepository.findByStatus(
                vn.gov.prison.secure.domain.model.prisoner.PrisonerStatus.ACTIVE);

        java.util.List<PotentialMatch> matches = new java.util.ArrayList<>();

        for (Prisoner prisoner : activePrisoners) {
            prisoner.getLatestBiometricData().ifPresent(storedBiometric -> {
                double matchScore = biometricVerificationService.calculateMatchScore(
                        biometricData, storedBiometric);

                if (matchScore >= 0.80) { // Threshold for potential match
                    matches.add(new PotentialMatch(
                            prisoner.getId(),
                            prisoner.getPrisonerNumber(),
                            prisoner.getDemographicInfo().getFullName(),
                            matchScore));
                }
            });
        }

        // Sort by match score descending
        matches.sort((m1, m2) -> Double.compare(m2.matchScore(), m1.matchScore()));

        return matches;
    }

    /**
     * Check for duplicate identity
     * Business rule: Flag if biometric match >= 98% with different prisoner
     */
    public DuplicateCheckResult checkForDuplicates(BiometricData biometricData,
            vn.gov.prison.secure.domain.model.prisoner.PrisonerId excludePrisonerId) {
        var potentialMatches = searchByBiometric(biometricData);

        var duplicates = potentialMatches.stream()
                .filter(match -> !match.prisonerId().equals(excludePrisonerId))
                .filter(match -> match.matchScore() >= 0.98)
                .toList();

        return new DuplicateCheckResult(
                !duplicates.isEmpty(),
                duplicates);
    }

    /**
     * Identity Verification Result
     */
    public record IdentityVerificationResult(
            vn.gov.prison.secure.domain.model.prisoner.PrisonerId prisonerId,
            boolean verified,
            double confidenceScore,
            BiometricData.BiometricType biometricType,
            boolean highConfidence) {
    }

    /**
     * Potential biometric match
     */
    public record PotentialMatch(
            vn.gov.prison.secure.domain.model.prisoner.PrisonerId prisonerId,
            String prisonerNumber,
            String fullName,
            double matchScore) {
    }

    /**
     * Duplicate check result
     */
    public record DuplicateCheckResult(
            boolean hasDuplicates,
            java.util.List<PotentialMatch> duplicates) {
    }
}
