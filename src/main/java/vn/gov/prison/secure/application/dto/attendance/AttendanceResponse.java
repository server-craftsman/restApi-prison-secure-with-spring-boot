package vn.gov.prison.secure.application.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {
    private UUID id;
    private UUID prisonerId;
    private String prisonerName;
    private LocalDate attendanceDate;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private LocalDateTime expectedCheckInTime;
    private LocalDateTime expectedCheckOutTime;
    private String status; // ON_TIME, LATE, MISSED, COMPLETED
    private Integer lateMinutes;
    private Integer vibrationLevel;
    private Boolean vibrationTriggered;
    private String vibrationMessage; // User-friendly message
    private String notes;
}
