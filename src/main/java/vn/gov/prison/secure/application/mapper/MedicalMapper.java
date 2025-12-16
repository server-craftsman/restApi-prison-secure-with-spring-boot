package vn.gov.prison.secure.application.mapper;

import org.springframework.stereotype.Component;
import vn.gov.prison.secure.application.dto.medical.CreateMedicalRecordRequest;
import vn.gov.prison.secure.application.dto.medical.MedicalRecordResponse;
import vn.gov.prison.secure.application.dto.medical.PrescriptionResponse;
import vn.gov.prison.secure.domain.model.medical.*;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;

import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MedicalMapper {
    
    public MedicalRecord toDomain(CreateMedicalRecordRequest request) {
        return MedicalRecord.builder()
                .prisonerId(PrisonerId.of(request.getPrisonerId().toString()))
                .recordDate(request.getRecordDate())
                .recordType(RecordType.valueOf(request.getRecordType()))
                .diagnosis(request.getDiagnosis())
                .treatment(request.getTreatment())
                .medicalStaff(MedicalStaff.of(
                        request.getMedicalStaffName(),
                        request.getMedicalStaffRole() != null ? request.getMedicalStaffRole() : "Doctor"))
                .notes(request.getNotes())
                .severity(request.getSeverity() != null ? Severity.valueOf(request.getSeverity()) : null)
                .followUpRequired(request.getFollowUpRequired() != null && request.getFollowUpRequired())
                .followUpDate(request.getFollowUpDate())
                .build();
    }
    
    public MedicalRecordResponse toResponse(MedicalRecord domain) {
        return MedicalRecordResponse.builder()
                .id(domain.getId().getValue())
                .prisonerId(UUID.fromString(domain.getPrisonerId().getValue()))
                .recordDate(domain.getRecordDate())
                .recordType(domain.getRecordType().name())
                .diagnosis(domain.getDiagnosis())
                .treatment(domain.getTreatment())
                .medicalStaffName(domain.getMedicalStaff().getName())
                .medicalStaffRole(domain.getMedicalStaff().getRole())
                .notes(domain.getNotes())
                .severity(domain.getSeverity() != null ? domain.getSeverity().name() : null)
                .followUpRequired(domain.isFollowUpRequired())
                .followUpDate(domain.getFollowUpDate())
                .prescriptions(domain.getPrescriptions() != null ? domain.getPrescriptions().stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }
    
    public PrescriptionResponse toResponse(Prescription domain) {
        return PrescriptionResponse.builder()
                .id(domain.getId())
                .prisonerId(UUID.fromString(domain.getPrisonerId().getValue()))
                .medicationName(domain.getMedicationName())
                .dosage(domain.getDosage())
                .frequency(domain.getFrequency())
                .route(domain.getRoute())
                .startDate(domain.getStartDate())
                .endDate(domain.getEndDate())
                .prescribingDoctor(domain.getPrescribingDoctor())
                .pharmacyNotes(domain.getPharmacyNotes())
                .status(domain.getStatus().name())
                .build();
    }
}
