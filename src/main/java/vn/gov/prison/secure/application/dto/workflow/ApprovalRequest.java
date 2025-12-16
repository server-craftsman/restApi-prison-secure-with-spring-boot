package vn.gov.prison.secure.application.dto.workflow;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequest {

    @NotBlank(message = "Action is required (APPROVE or REJECT)")
    private String action;

    private String comments;

    @NotBlank(message = "Approver name is required")
    private String approverName;

    private String approverRole;
}
