package vn.gov.prison.secure.application.usecase.medical;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.medical.MedicalRecordResponse;
import vn.gov.prison.secure.application.dto.medical.UpdateMedicalRecordRequest;
import vn.gov.prison.secure.application.mapper.MedicalMapper;
import vn.gov.prison.secure.domain.model.medical.MedicalRecord;
import vn.gov.prison.secure.domain.model.medical.MedicalRecordId;
import vn.gov.prison.secure.domain.model.medical.Severity;
import vn.gov.prison.secure.domain.repository.MedicalRecordRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateMedicalRecordUseCase {

    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalMapper medicalMapper;

    @Transactional
    public MedicalRecordResponse execute(UUID recordId, UpdateMedicalRecordRequest request) {
        MedicalRecord record = medicalRecordRepository.findById(MedicalRecordId.of(recordId))
                .orElseThrow(() -> new IllegalArgumentException("Medical record not found: " + recordId));

        // Update diagnosis and treatment together
        if (request.getDiagnosis() != null || request.getTreatment() != null) {
            String diagnosis = request.getDiagnosis() != null ? request.getDiagnosis() : record.getDiagnosis();
            String treatment = request.getTreatment() != null ? request.getTreatment() : record.getTreatment();
            record.updateDiagnosisAndTreatment(diagnosis, treatment);
        }

        // Update notes
        if (request.getNotes() != null) {
            record.addNotes(request.getNotes());
        }

        // Update severity
        if (request.getSeverity() != null) {
            record.updateSeverity(Severity.valueOf(request.getSeverity()));
        }

        // Update follow-up
        if (request.getFollowUpRequired() != null && request.getFollowUpRequired()
                && request.getFollowUpDate() != null) {
            record.scheduleFollowUp(request.getFollowUpDate());
        }

        MedicalRecord updated = medicalRecordRepository.save(record);
        return medicalMapper.toResponse(updated);
    }
}
