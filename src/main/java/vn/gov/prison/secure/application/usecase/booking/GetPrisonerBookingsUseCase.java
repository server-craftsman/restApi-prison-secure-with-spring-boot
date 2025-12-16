package vn.gov.prison.secure.application.usecase.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.booking.BookingResponse;
import vn.gov.prison.secure.application.mapper.BookingMapper;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;
import vn.gov.prison.secure.domain.repository.BookingRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetPrisonerBookingsUseCase {

    private final BookingRepository bookingRepository;
    private final BookingMapper mapper;

    @Transactional(readOnly = true)
    public List<BookingResponse> execute(UUID prisonerId) {
        return bookingRepository.findByPrisonerId(PrisonerId.of(prisonerId.toString()))
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
