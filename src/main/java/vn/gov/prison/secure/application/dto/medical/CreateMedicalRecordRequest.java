package vn.gov.prison.secure.application.dto.medical;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Request DTO for creating a medical record
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMedicalRecordRequest {

    @NotNull(message = "Prisoner ID is required")
    private UUID prisonerId;

    private LocalDateTime recordDate;

    @NotBlank(message = "Record type is required")
    private String recordType; // GENERAL, EMERGENCY, ROUTINE_CHECKUP, etc.

    private String diagnosis;

    private String treatment;

    @NotBlank(message = "Medical staff name is required")
    private String medicalStaffName;

    private String medicalStaffRole;

    private String notes;

    private String severity; // LOW, MEDIUM, HIGH, CRITICAL

    private Boolean followUpRequired;

    private LocalDate followUpDate;
}
