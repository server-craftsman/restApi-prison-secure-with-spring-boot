package vn.gov.prison.secure.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.gov.prison.secure.domain.attendance.AttendanceRecord;
import vn.gov.prison.secure.domain.attendance.AttendanceId;
import vn.gov.prison.secure.domain.attendance.AttendanceRepository;
import vn.gov.prison.secure.domain.attendance.AttendanceStatus;
import vn.gov.prison.secure.domain.prisoner.PrisonerId;
import vn.gov.prison.secure.infrastructure.persistence.entity.AttendanceRecordJpaEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaAttendanceRepositoryAdapter implements AttendanceRepository {

    private final SpringDataAttendanceRepository jpaRepository;

    @Override
    public AttendanceRecord save(AttendanceRecord attendanceRecord) {
        AttendanceRecordJpaEntity entity = toEntity(attendanceRecord);
        AttendanceRecordJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<AttendanceRecord> findById(AttendanceId id) {
        return jpaRepository.findById(id.getValue())
                .map(this::toDomain);
    }

    @Override
    public Optional<AttendanceRecord> findByPrisonerAndDate(PrisonerId prisonerId, LocalDate date) {
        return jpaRepository.findByPrisonerIdAndAttendanceDate(prisonerId.getValue().toString(), date)
                .map(this::toDomain);
    }

    @Override
    public List<AttendanceRecord> findByPrisoner(PrisonerId prisonerId) {
        return jpaRepository.findByPrisonerId(prisonerId.getValue().toString())
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceRecord> findPendingForDate(LocalDate date) {
        return jpaRepository.findByAttendanceDateAndStatus(date, AttendanceStatus.PENDING.name())
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceRecord> findMissedForDate(LocalDate date) {
        return jpaRepository.findByAttendanceDateAndStatus(date, AttendanceStatus.MISSED.name())
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceRecord> findByStatus(AttendanceStatus status) {
        return jpaRepository.findByStatus(status.name())
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private AttendanceRecordJpaEntity toEntity(AttendanceRecord domain) {
        return AttendanceRecordJpaEntity.builder()
                .id(domain.getId().getValue())
                .prisonerId(domain.getPrisonerId().getValue().toString())
                .attendanceDate(domain.getAttendanceDate())
                .checkInTime(domain.getCheckInTime())
                .checkOutTime(domain.getCheckOutTime())
                .expectedCheckInTime(domain.getExpectedCheckInTime())
                .expectedCheckOutTime(domain.getExpectedCheckOutTime())
                .status(domain.getStatus().name())
                .lateMinutes(domain.getLateMinutes())
                .vibrationLevel(domain.getVibrationLevel())
                .vibrationTriggered(domain.getVibrationTriggered())
                .notes(domain.getNotes())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    private AttendanceRecord toDomain(AttendanceRecordJpaEntity entity) {
        return AttendanceRecord.builder()
                .id(AttendanceId.of(entity.getId()))
                .prisonerId(PrisonerId.of(entity.getPrisonerId()))
                .attendanceDate(entity.getAttendanceDate())
                .checkInTime(entity.getCheckInTime())
                .checkOutTime(entity.getCheckOutTime())
                .expectedCheckInTime(entity.getExpectedCheckInTime())
                .expectedCheckOutTime(entity.getExpectedCheckOutTime())
                .status(AttendanceStatus.valueOf(entity.getStatus()))
                .lateMinutes(entity.getLateMinutes())
                .vibrationLevel(entity.getVibrationLevel())
                .vibrationTriggered(entity.getVibrationTriggered())
                .notes(entity.getNotes())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
