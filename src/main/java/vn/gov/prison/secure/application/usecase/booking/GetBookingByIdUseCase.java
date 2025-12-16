package vn.gov.prison.secure.application.usecase.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.booking.BookingResponse;
import vn.gov.prison.secure.application.mapper.BookingMapper;
import vn.gov.prison.secure.domain.model.booking.Booking;
import vn.gov.prison.secure.domain.model.booking.BookingId;
import vn.gov.prison.secure.domain.repository.BookingRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetBookingByIdUseCase {

    private final BookingRepository bookingRepository;
    private final BookingMapper mapper;

    @Transactional(readOnly = true)
    public BookingResponse execute(UUID bookingId) {
        Booking booking = bookingRepository.findById(BookingId.of(bookingId.toString()))
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));

        return mapper.toResponse(booking);
    }
}
