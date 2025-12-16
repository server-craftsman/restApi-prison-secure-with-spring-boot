package vn.gov.prison.secure.application.usecase.medical;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.medical.CreateMedicalRecordRequest;
import vn.gov.prison.secure.application.dto.medical.MedicalRecordResponse;
import vn.gov.prison.secure.application.mapper.MedicalMapper;
import vn.gov.prison.secure.domain.model.medical.MedicalRecord;
import vn.gov.prison.secure.domain.repository.MedicalRecordRepository;

/**
 * Use case for creating a medical record
 */
@Service
@RequiredArgsConstructor
public class CreateMedicalRecordUseCase {

    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalMapper medicalMapper;

    /**
     * Execute use case to create medical record
     */
    @Transactional
    public MedicalRecordResponse execute(CreateMedicalRecordRequest request) {
        // Map request to domain
        MedicalRecord medicalRecord = medicalMapper.toDomain(request);

        // Save to repository
        MedicalRecord saved = medicalRecordRepository.save(medicalRecord);

        // Map to response
        return medicalMapper.toResponse(saved);
    }
}
