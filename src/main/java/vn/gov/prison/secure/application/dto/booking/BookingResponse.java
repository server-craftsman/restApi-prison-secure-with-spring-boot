package vn.gov.prison.secure.application.dto.booking;

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
public class BookingResponse {

    private UUID id;
    private UUID prisonerId;
    private String bookingNumber;
    private String bookingType;
    private LocalDateTime bookingDate;
    private String status;
    private String arrestingAgency;
    private String arrestLocation;
    private String bookingOfficer;
    private String charges;
    private String caseNumber;
    private String courtName;
    private LocalDateTime releaseDate;
    private String releaseType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
