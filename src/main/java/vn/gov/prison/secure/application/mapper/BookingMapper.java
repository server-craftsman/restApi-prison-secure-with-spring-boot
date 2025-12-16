package vn.gov.prison.secure.application.mapper;

import org.springframework.stereotype.Component;
import vn.gov.prison.secure.application.dto.booking.BookingResponse;
import vn.gov.prison.secure.application.dto.booking.CreateBookingRequest;
import vn.gov.prison.secure.domain.model.booking.Booking;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;

import java.util.UUID;

@Component
public class BookingMapper {

    public Booking toDomain(CreateBookingRequest request) {
        // Use factory method to create booking
        Booking booking = Booking.create(
                PrisonerId.of(request.getPrisonerId().toString()),
                generateBookingNumber(),
                Booking.BookingType.valueOf(request.getBookingType()),
                request.getArrestingAgency());

        return booking;
    }

    public BookingResponse toResponse(Booking domain) {
        return BookingResponse.builder()
                .id(UUID.fromString(domain.getId().getValue()))
                .prisonerId(UUID.fromString(domain.getPrisonerId().getValue()))
                .bookingNumber(domain.getBookingNumber())
                .bookingType(domain.getType().name())
                .bookingDate(domain.getBookingDate())
                .status(domain.getStatus().name())
                .arrestingAgency(domain.getArrestingAgency())
                .arrestLocation(domain.getArrestLocation())
                .releaseDate(domain.getActualReleaseDate())
                .build();
    }

    private String generateBookingNumber() {
        return "BK-" + System.currentTimeMillis();
    }
}
