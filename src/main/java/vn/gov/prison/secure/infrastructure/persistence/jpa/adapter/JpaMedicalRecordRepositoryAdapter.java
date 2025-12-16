package vn.gov.prison.secure.infrastructure.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.gov.prison.secure.domain.model.medical.MedicalRecord;
import vn.gov.prison.secure.domain.model.medical.MedicalRecordId;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;
import vn.gov.prison.secure.domain.repository.MedicalRecordRepository;
import vn.gov.prison.secure.infrastructure.persistence.jpa.entity.MedicalRecordJpaEntity;
import vn.gov.prison.secure.infrastructure.persistence.jpa.repository.SpringDataMedicalRecordRepository;
import vn.gov.prison.secure.infrastructure.persistence.mapper.MedicalPersistenceMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaMedicalRecordRepositoryAdapter implements MedicalRecordRepository {

    private final SpringDataMedicalRecordRepository jpaRepository;
    private final MedicalPersistenceMapper mapper;

    @Override
    public MedicalRecord save(MedicalRecord medicalRecord) {
        MedicalRecordJpaEntity jpaEntity = mapper.toJpaEntity(medicalRecord);
        MedicalRecordJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<MedicalRecord> findById(MedicalRecordId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<MedicalRecord> findByPrisonerId(PrisonerId prisonerId) {
        return jpaRepository.findByPrisonerId(prisonerId.getValue()).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalRecord> findByPrisonerIdAndRecordType(PrisonerId prisonerId, String recordType) {
        MedicalRecordJpaEntity.RecordTypeEnum typeEnum = MedicalRecordJpaEntity.RecordTypeEnum.valueOf(recordType);
        return jpaRepository.findByPrisonerIdAndRecordType(prisonerId.getValue(), typeEnum).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalRecord> findRecordsRequiringFollowUp() {
        return jpaRepository.findRecordsRequiringFollowUp().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(MedicalRecordId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsById(MedicalRecordId id) {
        return jpaRepository.existsById(id.getValue());
    }
}
