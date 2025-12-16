package vn.gov.prison.secure.application.dto.booking;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseBookingRequest {

    @NotBlank(message = "Release type is required")
    private String releaseType;

    private String releaseReason;

    @NotBlank(message = "Released by is required")
    private String releasedBy;
}
