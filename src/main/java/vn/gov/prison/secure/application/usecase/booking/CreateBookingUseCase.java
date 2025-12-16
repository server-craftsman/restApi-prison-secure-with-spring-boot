package vn.gov.prison.secure.application.usecase.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.booking.BookingResponse;
import vn.gov.prison.secure.application.dto.booking.CreateBookingRequest;
import vn.gov.prison.secure.application.mapper.BookingMapper;
import vn.gov.prison.secure.domain.model.booking.Booking;
import vn.gov.prison.secure.domain.repository.BookingRepository;

@Service
@RequiredArgsConstructor
public class CreateBookingUseCase {

    private final BookingRepository bookingRepository;
    private final BookingMapper mapper;

    @Transactional
    public BookingResponse execute(CreateBookingRequest request) {
        Booking booking = mapper.toDomain(request);
        Booking saved = bookingRepository.save(booking);
        return mapper.toResponse(saved);
    }
}
