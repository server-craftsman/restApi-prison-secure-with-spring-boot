package vn.gov.prison.secure.application.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for Prisoner information
 * Following DTO pattern
 */
public record PrisonerResponse(
        String id,
        String prisonerNumber,

        // Demographic Information
        String firstName,
        String lastName,
        String middleName,
        String fullName,
        LocalDate dateOfBirth,
        int age,
        String gender,
        String nationality,
        String placeOfBirth,
        String nationalIdNumber,

        // Physical Description
        Integer heightCm,
        Integer weightKg,
        String eyeColor,
        String hairColor,
        String bloodType,
        List<String> distinctiveMarks,
        List<String> scars,
        List<String> tattoos,

        // Status Information
        String status,
        LocalDateTime admissionDate,
        LocalDateTime releaseDate,
        long daysInCustody,

        // Location Information
        String assignedFacility,
        String currentBlock,
        String currentCell,

        // Biometric Information
        boolean hasHighQualityBiometrics,
        List<String> availableBiometricTypes,

        // Metadata
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy) {
}
