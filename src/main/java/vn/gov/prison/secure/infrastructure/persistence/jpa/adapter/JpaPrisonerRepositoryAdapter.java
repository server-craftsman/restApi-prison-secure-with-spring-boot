package vn.gov.prison.secure.infrastructure.persistence.jpa.adapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.domain.model.prisoner.Prisoner;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerStatus;
import vn.gov.prison.secure.domain.repository.PrisonerRepository;
import vn.gov.prison.secure.infrastructure.persistence.jpa.entity.PrisonerJpaEntity;
import vn.gov.prison.secure.infrastructure.persistence.jpa.repository.SpringDataPrisonerRepository;
import vn.gov.prison.secure.infrastructure.persistence.mapper.PrisonerPersistenceMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of PrisonerRepository interface
 * Adapts Spring Data JPA repository to domain repository interface
 * 
 * Following Adapter Pattern and DIP (Dependency Inversion Principle)
 * Infrastructure depends on domain interface, not vice versa
 */
@Component
@Transactional
public class JpaPrisonerRepositoryAdapter implements PrisonerRepository {

    private final SpringDataPrisonerRepository springDataRepository;
    private final PrisonerPersistenceMapper mapper;

    public JpaPrisonerRepositoryAdapter(SpringDataPrisonerRepository springDataRepository,
            PrisonerPersistenceMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public Prisoner save(Prisoner prisoner) {
        PrisonerJpaEntity entity = mapper.toJpaEntity(prisoner);
        PrisonerJpaEntity saved = springDataRepository.save(entity);
        return mapper.toDomainEntity(saved);
    }

    @Override
    public Optional<Prisoner> findById(PrisonerId id) {
        return springDataRepository.findById(id.getValue())
                .map(mapper::toDomainEntity);
    }

    @Override
    public Optional<Prisoner> findByPrisonerNumber(String prisonerNumber) {
        return springDataRepository.findByPrisonerNumber(prisonerNumber)
                .map(mapper::toDomainEntity);
    }

    @Override
    public Optional<Prisoner> findByNationalIdNumber(String nationalIdNumber) {
        return springDataRepository.findByNationalIdNumber(nationalIdNumber)
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<Prisoner> findByStatus(PrisonerStatus status) {
        PrisonerJpaEntity.PrisonerStatus jpaStatus = PrisonerJpaEntity.PrisonerStatus.valueOf(status.name());

        return springDataRepository.findByStatus(jpaStatus).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Prisoner> findByFacility(String facilityId) {
        return springDataRepository.findByAssignedFacility(facilityId).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Prisoner> searchByName(String firstName, String lastName) {
        return springDataRepository.searchByName(firstName, lastName).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Prisoner> findAll(int page, int size) {
        return springDataRepository.findAll(PageRequest.of(page, size))
                .stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(PrisonerId id) {
        springDataRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsByPrisonerNumber(String prisonerNumber) {
        return springDataRepository.existsByPrisonerNumber(prisonerNumber);
    }

    @Override
    public long count() {
        return springDataRepository.count();
    }

    @Override
    public long countByStatus(PrisonerStatus status) {
        PrisonerJpaEntity.PrisonerStatus jpaStatus = PrisonerJpaEntity.PrisonerStatus.valueOf(status.name());

        return springDataRepository.countByStatus(jpaStatus);
    }
}
