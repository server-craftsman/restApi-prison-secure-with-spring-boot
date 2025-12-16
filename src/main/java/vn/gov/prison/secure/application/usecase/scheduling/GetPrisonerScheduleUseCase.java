package vn.gov.prison.secure.application.usecase.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.scheduling.ScheduleResponse;
import vn.gov.prison.secure.application.mapper.SchedulingMapper;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;
import vn.gov.prison.secure.domain.repository.ScheduleRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetPrisonerScheduleUseCase {

    private final ScheduleRepository scheduleRepository;
    private final SchedulingMapper mapper;

    @Transactional(readOnly = true)
    public List<ScheduleResponse> execute(UUID prisonerId) {
        return scheduleRepository.findByPrisonerId(PrisonerId.of(prisonerId.toString()))
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
