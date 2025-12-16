package vn.gov.prison.secure.infrastructure.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;
import vn.gov.prison.secure.domain.model.scheduling.Schedule;
import vn.gov.prison.secure.domain.model.scheduling.ScheduleId;
import vn.gov.prison.secure.domain.model.scheduling.ScheduleStatus;
import vn.gov.prison.secure.domain.model.scheduling.ScheduleType;
import vn.gov.prison.secure.domain.repository.ScheduleRepository;
import vn.gov.prison.secure.infrastructure.persistence.jpa.entity.ScheduleJpaEntity;
import vn.gov.prison.secure.infrastructure.persistence.jpa.repository.SpringDataScheduleRepository;
import vn.gov.prison.secure.infrastructure.persistence.mapper.SchedulingPersistenceMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaScheduleRepositoryAdapter implements ScheduleRepository {

    private final SpringDataScheduleRepository jpaRepository;
    private final SchedulingPersistenceMapper mapper;

    @Override
    public Schedule save(Schedule schedule) {
        ScheduleJpaEntity jpaEntity = mapper.toJpaEntity(schedule);
        ScheduleJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Schedule> findById(ScheduleId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<Schedule> findByPrisonerId(PrisonerId prisonerId) {
        return jpaRepository.findByPrisonerId(prisonerId.getValue()).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Schedule> findByPrisonerIdAndType(PrisonerId prisonerId, ScheduleType type) {
        ScheduleJpaEntity.ScheduleTypeEnum typeEnum = ScheduleJpaEntity.ScheduleTypeEnum.valueOf(type.name());
        return jpaRepository.findByPrisonerIdAndScheduleType(prisonerId.getValue(), typeEnum).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Schedule> findByPrisonerIdAndStatus(PrisonerId prisonerId, ScheduleStatus status) {
        ScheduleJpaEntity.ScheduleStatusEnum statusEnum = ScheduleJpaEntity.ScheduleStatusEnum.valueOf(status.name());
        return jpaRepository.findByPrisonerIdAndStatus(prisonerId.getValue(), statusEnum).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Schedule> findUpcomingSchedules(LocalDateTime from) {
        return jpaRepository.findUpcomingSchedules(from).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Schedule> findByTypeAndStatus(ScheduleType type, ScheduleStatus status) {
        ScheduleJpaEntity.ScheduleTypeEnum typeEnum = ScheduleJpaEntity.ScheduleTypeEnum.valueOf(type.name());
        ScheduleJpaEntity.ScheduleStatusEnum statusEnum = ScheduleJpaEntity.ScheduleStatusEnum.valueOf(status.name());
        return jpaRepository.findByScheduleTypeAndStatus(typeEnum, statusEnum).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(ScheduleId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsById(ScheduleId id) {
        return jpaRepository.existsById(id.getValue());
    }
}
