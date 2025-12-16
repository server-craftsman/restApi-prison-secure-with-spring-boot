package vn.gov.prison.secure.application.dto.booking;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateBookingRequest {

    @NotNull(message = "Prisoner ID is required")
    private UUID prisonerId;

    @NotBlank(message = "Booking type is required")
    private String bookingType;

    @NotNull(message = "Booking date is required")
    private LocalDateTime bookingDate;

    private String arrestingAgency;
    private String arrestLocation;
    private String bookingOfficer;
    private String charges;
    private String caseNumber;
    private String courtName;

    @NotBlank(message = "Created by is required")
    private String createdBy;
}
