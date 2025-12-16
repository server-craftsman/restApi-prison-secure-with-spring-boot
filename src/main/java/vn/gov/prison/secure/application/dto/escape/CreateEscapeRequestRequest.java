package vn.gov.prison.secure.application.dto.escape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEscapeRequestRequest {
    private UUID prisonerId;
    private String reason;
    private LocalDate plannedEscapeDate;
}
