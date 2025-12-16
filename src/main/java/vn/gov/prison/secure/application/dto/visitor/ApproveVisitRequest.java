package vn.gov.prison.secure.application.dto.visitor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApproveVisitRequest {

    private Boolean approved;
    private String approvedBy;
    private String rejectionReason;
    private String notes;
}
