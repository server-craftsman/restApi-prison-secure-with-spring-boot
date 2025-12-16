package vn.gov.prison.secure.application.usecase.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.domain.model.scheduling.Schedule;
import vn.gov.prison.secure.domain.model.scheduling.ScheduleId;
import vn.gov.prison.secure.domain.repository.ScheduleRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CancelScheduleUseCase {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void execute(UUID scheduleId) {
        Schedule schedule = scheduleRepository.findById(ScheduleId.of(scheduleId))
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found: " + scheduleId));

        schedule.cancel();
        scheduleRepository.save(schedule);
    }
}
