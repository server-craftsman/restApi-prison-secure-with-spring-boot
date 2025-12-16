package vn.gov.prison.secure.application.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingRequest {

    private String status;
    private String charges;
    private String caseNumber;
    private String courtName;
    private String updatedBy;
}
