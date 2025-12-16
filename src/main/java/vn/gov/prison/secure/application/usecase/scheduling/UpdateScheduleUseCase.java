package vn.gov.prison.secure.application.usecase.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.scheduling.ScheduleResponse;
import vn.gov.prison.secure.application.dto.scheduling.UpdateScheduleRequest;
import vn.gov.prison.secure.application.mapper.SchedulingMapper;
import vn.gov.prison.secure.domain.model.scheduling.*;
import vn.gov.prison.secure.domain.repository.ScheduleRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateScheduleUseCase {

    private final ScheduleRepository scheduleRepository;
    private final SchedulingMapper mapper;

    @Transactional
    public ScheduleResponse execute(UUID scheduleId, UpdateScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(ScheduleId.of(scheduleId))
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found: " + scheduleId));

        // Update fields if provided
        if (request.getTitle() != null || request.getDescription() != null || request.getLocation() != null) {
            schedule.updateDetails(
                    request.getTitle() != null ? request.getTitle() : schedule.getTitle(),
                    request.getDescription() != null ? request.getDescription() : schedule.getDescription(),
                    request.getLocation() != null ? request.getLocation() : schedule.getLocation());
        }

        // Update priority if provided
        if (request.getPriority() != null) {
            schedule.updatePriority(SchedulePriority.valueOf(request.getPriority()));
        }

        // Handle status changes
        if (request.getStatus() != null) {
            ScheduleStatus newStatus = ScheduleStatus.valueOf(request.getStatus());
            switch (newStatus) {
                case CONFIRMED -> schedule.confirm();
                case COMPLETED -> schedule.complete();
                case CANCELLED -> schedule.cancel();
                case NO_SHOW -> schedule.markNoShow();
                case RESCHEDULED -> {
                    if (request.getScheduledDate() != null) {
                        schedule.reschedule(request.getScheduledDate(), request.getEndDate());
                    }
                }
            }
        }

        Schedule updated = scheduleRepository.save(schedule);
        return mapper.toResponse(updated);
    }
}
