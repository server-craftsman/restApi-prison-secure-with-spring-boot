package vn.gov.prison.secure.application.usecase.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.booking.BookingResponse;
import vn.gov.prison.secure.application.dto.booking.ReleaseBookingRequest;
import vn.gov.prison.secure.application.mapper.BookingMapper;
import vn.gov.prison.secure.domain.model.booking.Booking;
import vn.gov.prison.secure.domain.model.booking.BookingId;
import vn.gov.prison.secure.domain.repository.BookingRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReleaseBookingUseCase {

    private final BookingRepository bookingRepository;
    private final BookingMapper mapper;

    @Transactional
    public BookingResponse execute(UUID bookingId, ReleaseBookingRequest request) {
        Booking booking = bookingRepository.findById(BookingId.of(bookingId.toString()))
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));

        String reason = request.getReleaseType() + ": "
                + (request.getReleaseReason() != null ? request.getReleaseReason() : "");
        booking.release(reason);

        Booking updated = bookingRepository.save(booking);
        return mapper.toResponse(updated);
    }
}
