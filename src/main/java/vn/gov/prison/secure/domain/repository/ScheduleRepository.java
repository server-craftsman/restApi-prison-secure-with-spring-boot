package vn.gov.prison.secure.domain.repository;

import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;
import vn.gov.prison.secure.domain.model.scheduling.Schedule;
import vn.gov.prison.secure.domain.model.scheduling.ScheduleId;
import vn.gov.prison.secure.domain.model.scheduling.ScheduleStatus;
import vn.gov.prison.secure.domain.model.scheduling.ScheduleType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Domain repository interface for Schedule aggregate
 */
public interface ScheduleRepository {

    Schedule save(Schedule schedule);

    Optional<Schedule> findById(ScheduleId id);

    List<Schedule> findByPrisonerId(PrisonerId prisonerId);

    List<Schedule> findByPrisonerIdAndType(PrisonerId prisonerId, ScheduleType type);

    List<Schedule> findByPrisonerIdAndStatus(PrisonerId prisonerId, ScheduleStatus status);

    List<Schedule> findUpcomingSchedules(LocalDateTime from);

    List<Schedule> findByTypeAndStatus(ScheduleType type, ScheduleStatus status);

    void delete(ScheduleId id);

    boolean existsById(ScheduleId id);
}
