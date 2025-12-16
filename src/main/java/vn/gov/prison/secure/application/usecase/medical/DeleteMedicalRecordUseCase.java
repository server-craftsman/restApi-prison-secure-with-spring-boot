package vn.gov.prison.secure.application.usecase.medical;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.domain.model.medical.MedicalRecordId;
import vn.gov.prison.secure.domain.repository.MedicalRecordRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteMedicalRecordUseCase {

    private final MedicalRecordRepository medicalRecordRepository;

    @Transactional
    public void execute(UUID recordId) {
        MedicalRecordId medicalRecordId = MedicalRecordId.of(recordId);

        // Verify record exists
        medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new IllegalArgumentException("Medical record not found: " + recordId));

        medicalRecordRepository.delete(medicalRecordId);
    }
}
