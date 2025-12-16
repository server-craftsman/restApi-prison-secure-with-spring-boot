package vn.gov.prison.secure.application.dto.escape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApproveEscapeRequestRequest {
    private Boolean approved;
    private String notes;
}
