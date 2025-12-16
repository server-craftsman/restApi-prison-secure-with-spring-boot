package vn.gov.prison.secure.application.usecase.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.scheduling.CreateScheduleRequest;
import vn.gov.prison.secure.application.dto.scheduling.ScheduleResponse;
import vn.gov.prison.secure.application.mapper.SchedulingMapper;
import vn.gov.prison.secure.domain.model.scheduling.Schedule;
import vn.gov.prison.secure.domain.repository.ScheduleRepository;

@Service
@RequiredArgsConstructor
public class CreateScheduleUseCase {

    private final ScheduleRepository scheduleRepository;
    private final SchedulingMapper mapper;

    @Transactional
    public ScheduleResponse execute(CreateScheduleRequest request) {
        Schedule schedule = mapper.toDomain(request);
        Schedule saved = scheduleRepository.save(schedule);
        return mapper.toResponse(saved);
    }
}
