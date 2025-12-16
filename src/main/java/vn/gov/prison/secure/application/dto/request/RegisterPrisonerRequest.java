package vn.gov.prison.secure.application.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * Request DTO for registering a new prisoner
 * Following DTO pattern for data transfer between layers
 */
public record RegisterPrisonerRequest(

        // Demographic Information
        @NotBlank(message = "First name is required") String firstName,

        @NotBlank(message = "Last name is required") String lastName,

        String middleName,

        @NotNull(message = "Date of birth is required") @Past(message = "Date of birth must be in the past") LocalDate dateOfBirth,

        @NotNull(message = "Gender is required") String gender,

        String nationality,
        String placeOfBirth,
        String nationalIdNumber,

        // Physical Description
        @Min(value = 50, message = "Height must be at least 50 cm") @Max(value = 300, message = "Height must not exceed 300 cm") Integer heightCm,

        @Min(value = 10, message = "Weight must be at least 10 kg") @Max(value = 500, message = "Weight must not exceed 500 kg") Integer weightKg,

        String eyeColor,
        String hairColor,
        String bloodType,
        java.util.List<String> distinctiveMarks,
        java.util.List<String> scars,
        java.util.List<String> tattoos,

        // Booking Information
        @NotBlank(message = "Prisoner number is required") String prisonerNumber,

        String assignedFacility) {
}
