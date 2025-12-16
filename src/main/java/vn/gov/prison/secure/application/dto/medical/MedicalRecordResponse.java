package vn.gov.prison.secure.application.dto.medical;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for medical record
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordResponse {

    private UUID id;
    private UUID prisonerId;
    private LocalDateTime recordDate;
    private String recordType;
    private String diagnosis;
    private String treatment;
    private String medicalStaffName;
    private String medicalStaffRole;
    private String notes;
    private String severity;
    private Boolean followUpRequired;
    private LocalDate followUpDate;
    private List<PrescriptionResponse> prescriptions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
