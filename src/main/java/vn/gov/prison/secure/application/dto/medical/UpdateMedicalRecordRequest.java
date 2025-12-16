package vn.gov.prison.secure.application.dto.medical;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMedicalRecordRequest {

    private String diagnosis;
    private String treatment;
    private String notes;
    private String severity;
    private Boolean followUpRequired;
    private LocalDate followUpDate;

    @NotBlank(message = "Updated by is required")
    private String updatedBy;
}
