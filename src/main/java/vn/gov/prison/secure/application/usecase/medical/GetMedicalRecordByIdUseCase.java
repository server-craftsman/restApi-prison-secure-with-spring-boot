package vn.gov.prison.secure.application.usecase.medical;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.medical.MedicalRecordResponse;
import vn.gov.prison.secure.application.mapper.MedicalMapper;
import vn.gov.prison.secure.domain.model.medical.MedicalRecord;
import vn.gov.prison.secure.domain.model.medical.MedicalRecordId;
import vn.gov.prison.secure.domain.repository.MedicalRecordRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetMedicalRecordByIdUseCase {

    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalMapper medicalMapper;

    @Transactional(readOnly = true)
    public MedicalRecordResponse execute(UUID recordId) {
        MedicalRecord record = medicalRecordRepository.findById(MedicalRecordId.of(recordId))
                .orElseThrow(() -> new IllegalArgumentException("Medical record not found: " + recordId));

        return medicalMapper.toResponse(record);
    }
}
