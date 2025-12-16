package vn.gov.prison.secure.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.gov.prison.secure.infrastructure.persistence.entity.AttendanceRecordJpaEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataAttendanceRepository extends JpaRepository<AttendanceRecordJpaEntity, UUID> {

    Optional<AttendanceRecordJpaEntity> findByPrisonerIdAndAttendanceDate(String prisonerId, LocalDate date);

    List<AttendanceRecordJpaEntity> findByPrisonerId(String prisonerId);

    List<AttendanceRecordJpaEntity> findByStatus(String status);

    List<AttendanceRecordJpaEntity> findByAttendanceDateAndStatus(LocalDate date, String status);
}
