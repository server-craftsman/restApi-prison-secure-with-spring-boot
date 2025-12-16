package vn.gov.prison.secure.domain.repository;

import vn.gov.prison.secure.domain.model.booking.Booking;
import vn.gov.prison.secure.domain.model.booking.BookingId;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Booking aggregate
 */
public interface BookingRepository {

    Booking save(Booking booking);

    Optional<Booking> findById(BookingId id);

    Optional<Booking> findByBookingNumber(String bookingNumber);

    List<Booking> findByPrisonerId(PrisonerId prisonerId);

    List<Booking> findByStatus(Booking.BookingStatus status);

    List<Booking> findByDateRange(LocalDateTime start, LocalDateTime end);

    List<Booking> findActiveBookings();

    List<Booking> findWithPendingCourtAppearances();

    List<Booking> findAll(int page, int size);

    void delete(BookingId id);

    boolean existsByBookingNumber(String bookingNumber);

    long count();
}
