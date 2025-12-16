package vn.gov.prison.secure.application.dto.visitor;

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
public class CreateVisitRequestRequest {

    private UUID prisonerId;
    private UUID visitorId;
    private LocalDateTime requestedDate;
    private Integer durationMinutes;
    private String purpose;
    private String relationship;
    private String requestedBy;
}
