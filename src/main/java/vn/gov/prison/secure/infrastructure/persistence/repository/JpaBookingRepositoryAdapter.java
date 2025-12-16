package vn.gov.prison.secure.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import vn.gov.prison.secure.domain.model.booking.Booking;
import vn.gov.prison.secure.domain.model.booking.BookingId;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;
import vn.gov.prison.secure.domain.repository.BookingRepository;
import vn.gov.prison.secure.infrastructure.persistence.entity.BookingJpaEntity;
import vn.gov.prison.secure.infrastructure.persistence.mapper.BookingPersistenceMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaBookingRepositoryAdapter implements BookingRepository {

    private final SpringDataBookingRepository jpaRepository;
    private final BookingPersistenceMapper mapper;

    @Override
    public Booking save(Booking booking) {
        BookingJpaEntity entity = mapper.toEntity(booking);
        BookingJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Booking> findById(BookingId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Booking> findByBookingNumber(String bookingNumber) {
        return jpaRepository.findByBookingNumber(bookingNumber)
                .map(mapper::toDomain);
    }

    @Override
    public List<Booking> findByPrisonerId(PrisonerId prisonerId) {
        return jpaRepository.findByPrisonerId(prisonerId.getValue())
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByStatus(Booking.BookingStatus status) {
        return jpaRepository.findByStatus(status.name())
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return jpaRepository.findByDateRange(start, end)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findActiveBookings() {
        return jpaRepository.findActiveBookings()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findWithPendingCourtAppearances() {
        // This would require a more complex query - returning empty list for now
        return List.of();
    }

    @Override
    public List<Booking> findAll(int page, int size) {
        return jpaRepository.findAll(PageRequest.of(page, size))
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(BookingId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsByBookingNumber(String bookingNumber) {
        return jpaRepository.existsByBookingNumber(bookingNumber);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}
