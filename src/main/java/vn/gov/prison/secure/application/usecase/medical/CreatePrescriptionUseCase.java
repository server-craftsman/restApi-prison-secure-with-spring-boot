package vn.gov.prison.secure.application.usecase.medical;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.medical.CreatePrescriptionRequest;
import vn.gov.prison.secure.application.dto.medical.PrescriptionResponse;
import vn.gov.prison.secure.application.mapper.MedicalMapper;
import vn.gov.prison.secure.domain.model.medical.*;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;
import vn.gov.prison.secure.domain.repository.MedicalRecordRepository;

@Service
@RequiredArgsConstructor
public class CreatePrescriptionUseCase {

    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalMapper medicalMapper;

    @Transactional
    public PrescriptionResponse execute(CreatePrescriptionRequest request) {
        MedicalRecord record = medicalRecordRepository.findById(MedicalRecordId.of(request.getMedicalRecordId()))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Medical record not found: " + request.getMedicalRecordId()));

        Prescription prescription = Prescription.builder()
                .id(java.util.UUID.randomUUID())
                .prisonerId(PrisonerId.of(request.getPrisonerId().toString()))
                .medicationName(request.getMedicationName())
                .dosage(request.getDosage())
                .frequency(request.getFrequency())
                .route(request.getRoute())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .prescribingDoctor(request.getPrescribingDoctor())
                .pharmacyNotes(request.getPharmacyNotes())
                .status(PrescriptionStatus.ACTIVE)
                .build();

        record.addPrescription(prescription);
        MedicalRecord updated = medicalRecordRepository.save(record);

        // Find the added prescription and return it
        Prescription addedPrescription = updated.getPrescriptions().stream()
                .filter(p -> p.getMedicationName().equals(request.getMedicationName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Prescription not found after save"));

        return medicalMapper.toResponse(addedPrescription);
    }
}
