package vn.gov.prison.secure.application.dto.escape;

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
public class EscapeRequestResponse {
    private UUID id;
    private UUID prisonerId;
    private String prisonerName;
    private String prisonerNumber;
    private LocalDateTime requestDate;
    private String reason;
    private LocalDate plannedEscapeDate;
    private String status;

    // Guard approval info
    private LocalDateTime guardApprovalDate;
    private String guardApprovalNotes;
    private String guardName;

    // Warden approval info
    private LocalDateTime wardenApprovalDate;
    private String wardenApprovalNotes;
    private String wardenName;

    // Execution info
    private LocalDateTime executionDate;
    private String executionNotes;
}
