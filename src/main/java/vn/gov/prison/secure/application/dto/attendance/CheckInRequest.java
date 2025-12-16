package vn.gov.prison.secure.application.dto.attendance;

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
public class CheckInRequest {
    private UUID prisonerId;
    private LocalDateTime checkInTime;
    private String tabletId; // For verification
}
