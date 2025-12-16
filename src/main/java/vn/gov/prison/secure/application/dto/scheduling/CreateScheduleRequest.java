package vn.gov.prison.secure.application.dto.scheduling;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateScheduleRequest {

    private UUID prisonerId;

    @NotBlank(message = "Schedule type is required")
    private String scheduleType;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Scheduled date is required")
    private LocalDateTime scheduledDate;

    private LocalDateTime endDate;

    private Integer durationMinutes;

    private String location;

    private String priority;

    private LocalDateTime reminderDate;

    @NotBlank(message = "Created by is required")
    private String createdBy;
}
