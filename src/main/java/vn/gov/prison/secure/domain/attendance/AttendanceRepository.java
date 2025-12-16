package vn.gov.prison.secure.domain.attendance;

import vn.gov.prison.secure.domain.prisoner.PrisonerId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository {
    AttendanceRecord save(AttendanceRecord record);

    Optional<AttendanceRecord> findById(AttendanceId id);

    Optional<AttendanceRecord> findByPrisonerAndDate(PrisonerId prisonerId, LocalDate date);

    List<AttendanceRecord> findByPrisoner(PrisonerId prisonerId);

    List<AttendanceRecord> findByStatus(AttendanceStatus status);

    List<AttendanceRecord> findPendingForDate(LocalDate date);

    List<AttendanceRecord> findMissedForDate(LocalDate date);
}
