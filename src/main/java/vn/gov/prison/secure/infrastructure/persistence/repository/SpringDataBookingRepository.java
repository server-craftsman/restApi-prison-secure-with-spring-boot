package vn.gov.prison.secure.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.gov.prison.secure.infrastructure.persistence.entity.BookingJpaEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataBookingRepository extends JpaRepository<BookingJpaEntity, String> {

    Optional<BookingJpaEntity> findByBookingNumber(String bookingNumber);

    List<BookingJpaEntity> findByPrisonerId(String prisonerId);

    List<BookingJpaEntity> findByStatus(String status);

    @Query("SELECT b FROM BookingJpaEntity b WHERE b.bookingDate BETWEEN :start AND :end")
    List<BookingJpaEntity> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT b FROM BookingJpaEntity b WHERE b.status = 'ACTIVE'")
    List<BookingJpaEntity> findActiveBookings();

    boolean existsByBookingNumber(String bookingNumber);
}
