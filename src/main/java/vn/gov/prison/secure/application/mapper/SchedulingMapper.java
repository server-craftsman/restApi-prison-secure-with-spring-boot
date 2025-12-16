package vn.gov.prison.secure.application.mapper;

import org.springframework.stereotype.Component;
import vn.gov.prison.secure.application.dto.scheduling.CreateScheduleRequest;
import vn.gov.prison.secure.application.dto.scheduling.ScheduleResponse;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;
import vn.gov.prison.secure.domain.model.scheduling.*;

@Component
public class SchedulingMapper {

    public Schedule toDomain(CreateScheduleRequest request) {
        return Schedule.builder()
                .prisonerId(request.getPrisonerId() != null ? PrisonerId.of(request.getPrisonerId().toString()) : null)
                .scheduleType(ScheduleType.valueOf(request.getScheduleType()))
                .title(request.getTitle())
                .description(request.getDescription())
                .scheduledDate(request.getScheduledDate())
                .endDate(request.getEndDate())
                .durationMinutes(request.getDurationMinutes())
                .location(request.getLocation())
                .priority(request.getPriority() != null ? SchedulePriority.valueOf(request.getPriority())
                        : SchedulePriority.NORMAL)
                .reminderDate(request.getReminderDate())
                .createdBy(request.getCreatedBy())
                .build();
    }

    public ScheduleResponse toResponse(Schedule domain) {
        return ScheduleResponse.builder()
                .id(domain.getId().getValue())
                .prisonerId(
                        domain.getPrisonerId() != null ? java.util.UUID.fromString(domain.getPrisonerId().getValue())
                                : null)
                .scheduleType(domain.getScheduleType().name())
                .title(domain.getTitle())
                .description(domain.getDescription())
                .scheduledDate(domain.getScheduledDate())
                .endDate(domain.getEndDate())
                .durationMinutes(domain.getDurationMinutes())
                .location(domain.getLocation())
                .status(domain.getStatus().name())
                .priority(domain.getPriority().name())
                .reminderSent(domain.isReminderSent())
                .reminderDate(domain.getReminderDate())
                .createdBy(domain.getCreatedBy())
                .build();
    }
}
