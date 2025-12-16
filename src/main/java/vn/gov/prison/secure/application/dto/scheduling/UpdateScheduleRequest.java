package vn.gov.prison.secure.application.dto.scheduling;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateScheduleRequest {

    private String title;
    private String description;
    private LocalDateTime scheduledDate;
    private LocalDateTime endDate;
    private Integer durationMinutes;
    private String location;
    private String status;
    private String priority;

    @NotBlank(message = "Updated by is required")
    private String updatedBy;
}
