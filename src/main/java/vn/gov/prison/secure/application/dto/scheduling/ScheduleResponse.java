package vn.gov.prison.secure.application.dto.scheduling;

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
public class ScheduleResponse {

    private UUID id;
    private UUID prisonerId;
    private String scheduleType;
    private String title;
    private String description;
    private LocalDateTime scheduledDate;
    private LocalDateTime endDate;
    private Integer durationMinutes;
    private String location;
    private String status;
    private String priority;
    private Boolean reminderSent;
    private LocalDateTime reminderDate;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
