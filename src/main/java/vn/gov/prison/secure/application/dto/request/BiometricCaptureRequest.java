package vn.gov.prison.secure.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * Request DTO for capturing biometric data
 */
public record BiometricCaptureRequest(

        @NotNull(message = "Biometric type is required") String biometricType,

        @NotBlank(message = "Template data is required") String templateDataBase64, // Base64 encoded biometric template

        @NotBlank(message = "Format is required") String format, // WSQ, JPEG2000, PNG, etc.

        Integer qualityScore,

        Map<String, String> metadata,

        @NotBlank(message = "Captured by is required") String capturedBy,

        String quality // EXCELLENT, GOOD, FAIR, POOR, UNUSABLE
) {
}
