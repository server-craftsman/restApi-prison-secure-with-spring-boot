package vn.gov.prison.secure.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.gov.prison.secure.infrastructure.persistence.jpa.entity.ScheduleJpaEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for Schedules
 */
@Repository
public interface SpringDataScheduleRepository extends JpaRepository<ScheduleJpaEntity, UUID> {

    /**
     * Find all schedules for a prisoner
     */
    List<ScheduleJpaEntity> findByPrisonerId(String prisonerId);

    /**
     * Find schedules by prisoner and type
     */
    List<ScheduleJpaEntity> findByPrisonerIdAndScheduleType(
            String prisonerId,
            ScheduleJpaEntity.ScheduleTypeEnum scheduleType);

    /**
     * Find schedules by prisoner and status
     */
    List<ScheduleJpaEntity> findByPrisonerIdAndStatus(
            String prisonerId,
            ScheduleJpaEntity.ScheduleStatusEnum status);

    /**
     * Find upcoming schedules from a specific date
     */
    @Query("SELECT s FROM ScheduleJpaEntity s WHERE s.scheduledDate >= :from ORDER BY s.scheduledDate ASC")
    List<ScheduleJpaEntity> findUpcomingSchedules(@Param("from") LocalDateTime from);

    /**
     * Find schedules by type and status
     */
    List<ScheduleJpaEntity> findByScheduleTypeAndStatus(
            ScheduleJpaEntity.ScheduleTypeEnum scheduleType,
            ScheduleJpaEntity.ScheduleStatusEnum status);

    /**
     * Find schedules needing reminders
     */
    @Query("SELECT s FROM ScheduleJpaEntity s WHERE s.reminderSent = false AND s.reminderDate <= :now AND s.status IN ('SCHEDULED', 'CONFIRMED')")
    List<ScheduleJpaEntity> findSchedulesNeedingReminders(@Param("now") LocalDateTime now);
}
