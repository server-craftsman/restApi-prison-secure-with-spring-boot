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
public class VisitRequestResponse {

    private UUID id;
    private UUID prisonerId;
    private UUID visitorId;
    private LocalDateTime requestedDate;
    private Integer durationMinutes;
    private String status;
    private String purpose;
    private String relationship;
    private LocalDateTime createdAt;
    private String approvedBy;
    private LocalDateTime approvedAt;
    private String rejectionReason;
}
