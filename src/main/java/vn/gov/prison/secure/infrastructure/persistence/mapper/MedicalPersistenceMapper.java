package vn.gov.prison.secure.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;
import vn.gov.prison.secure.domain.model.medical.*;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;
import vn.gov.prison.secure.infrastructure.persistence.entity.HealthCheckupJpaEntity;
import vn.gov.prison.secure.infrastructure.persistence.entity.MedicalRecordJpaEntity;
import vn.gov.prison.secure.infrastructure.persistence.entity.PrescriptionJpaEntity;

import java.util.UUID;

@Component
public class MedicalPersistenceMapper {

    public MedicalRecordJpaEntity toJpaEntity(MedicalRecord domain) {
        if (domain == null)
            return null;

        return MedicalRecordJpaEntity.builder()
                .id(domain.getId().getValue())
                .prisonerId(domain.getPrisonerId().getValue())
                .recordDate(domain.getRecordDate())
                .recordType(mapRecordType(domain.getRecordType()))
                .diagnosis(domain.getDiagnosis())
                .treatment(domain.getTreatment())
                .medicalStaff(domain.getMedicalStaff().getName())
                .notes(domain.getNotes())
                .severity(mapSeverity(domain.getSeverity()))
                .followUpRequired(domain.isFollowUpRequired())
                .followUpDate(domain.getFollowUpDate())
                .build();
    }

    public MedicalRecord toDomain(MedicalRecordJpaEntity jpa) {
        if (jpa == null)
            return null;

        return MedicalRecord.builder()
                .id(MedicalRecordId.of(jpa.getId()))
                .prisonerId(PrisonerId.of(jpa.getPrisonerId()))
                .recordDate(jpa.getRecordDate())
                .recordType(mapRecordTypeEnum(jpa.getRecordType()))
                .diagnosis(jpa.getDiagnosis())
                .treatment(jpa.getTreatment())
                .medicalStaff(MedicalStaff.of(jpa.getMedicalStaff(), "Medical Staff"))
                .notes(jpa.getNotes())
                .severity(mapSeverityEnum(jpa.getSeverity()))
                .followUpRequired(jpa.getFollowUpRequired() != null && jpa.getFollowUpRequired())
                .followUpDate(jpa.getFollowUpDate())
                .build();
    }

    public PrescriptionJpaEntity toJpaEntity(Prescription domain) {
        if (domain == null)
            return null;

        return PrescriptionJpaEntity.builder()
                .id(domain.getId())
                .prisonerId(domain.getPrisonerId().getValue())
                .medicationName(domain.getMedicationName())
                .dosage(domain.getDosage())
                .frequency(domain.getFrequency())
                .route(mapRoute(domain.getRoute()))
                .startDate(domain.getStartDate())
                .endDate(domain.getEndDate())
                .prescribingDoctor(domain.getPrescribingDoctor())
                .pharmacyNotes(domain.getPharmacyNotes())
                .status(mapPrescriptionStatus(domain.getStatus()))
                .discontinueReason(domain.getDiscontinueReason())
                .build();
    }

    public Prescription toDomain(PrescriptionJpaEntity jpa) {
        if (jpa == null)
            return null;

        return Prescription.builder()
                .id(jpa.getId())
                .prisonerId(PrisonerId.of(jpa.getPrisonerId()))
                .medicationName(jpa.getMedicationName())
                .dosage(jpa.getDosage())
                .frequency(jpa.getFrequency())
                .route(jpa.getRoute() != null ? jpa.getRoute().name() : null)
                .startDate(jpa.getStartDate())
                .endDate(jpa.getEndDate())
                .prescribingDoctor(jpa.getPrescribingDoctor())
                .pharmacyNotes(jpa.getPharmacyNotes())
                .status(mapPrescriptionStatusEnum(jpa.getStatus()))
                .build();
    }

    public HealthCheckupJpaEntity toJpaEntity(HealthCheckup domain) {
        if (domain == null)
            return null;

        return HealthCheckupJpaEntity.builder()
                .id(domain.getId())
                .prisonerId(domain.getPrisonerId().getValue())
                .checkupDate(domain.getCheckupDate())
                .checkupType(mapCheckupType(domain.getCheckupType()))
                .bloodPressure(domain.getBloodPressure())
                .pulseRate(domain.getPulseRate())
                .temperature(domain.getTemperature())
                .weight(domain.getWeight())
                .height(domain.getHeight())
                .bmi(domain.getBmi())
                .bloodSugar(domain.getBloodSugar())
                .oxygenSaturation(domain.getOxygenSaturation())
                .respiratoryRate(domain.getRespiratoryRate())
                .generalCondition(mapGeneralCondition(domain.getGeneralCondition()))
                .notes(domain.getNotes())
                .nextCheckupDate(domain.getNextCheckupDate())
                .performedBy(domain.getPerformedBy())
                .location(domain.getLocation())
                .build();
    }

    public HealthCheckup toDomain(HealthCheckupJpaEntity jpa) {
        if (jpa == null)
            return null;

        return HealthCheckup.builder()
                .id(jpa.getId())
                .prisonerId(PrisonerId.of(jpa.getPrisonerId()))
                .checkupDate(jpa.getCheckupDate())
                .checkupType(mapCheckupTypeEnum(jpa.getCheckupType()))
                .bloodPressure(jpa.getBloodPressure())
                .pulseRate(jpa.getPulseRate())
                .temperature(jpa.getTemperature())
                .weight(jpa.getWeight())
                .height(jpa.getHeight())
                .bmi(jpa.getBmi())
                .bloodSugar(jpa.getBloodSugar())
                .oxygenSaturation(jpa.getOxygenSaturation())
                .respiratoryRate(jpa.getRespiratoryRate())
                .generalCondition(mapGeneralConditionEnum(jpa.getGeneralCondition()))
                .notes(jpa.getNotes())
                .nextCheckupDate(jpa.getNextCheckupDate())
                .performedBy(jpa.getPerformedBy())
                .location(jpa.getLocation())
                .build();
    }

    private MedicalRecordJpaEntity.RecordTypeEnum mapRecordType(RecordType type) {
        return type != null ? MedicalRecordJpaEntity.RecordTypeEnum.valueOf(type.name()) : null;
    }

    private RecordType mapRecordTypeEnum(MedicalRecordJpaEntity.RecordTypeEnum type) {
        return type != null ? RecordType.valueOf(type.name()) : null;
    }

    private MedicalRecordJpaEntity.SeverityEnum mapSeverity(Severity severity) {
        return severity != null ? MedicalRecordJpaEntity.SeverityEnum.valueOf(severity.name()) : null;
    }

    private Severity mapSeverityEnum(MedicalRecordJpaEntity.SeverityEnum severity) {
        return severity != null ? Severity.valueOf(severity.name()) : null;
    }

    private PrescriptionJpaEntity.RouteEnum mapRoute(String route) {
        return route != null ? PrescriptionJpaEntity.RouteEnum.valueOf(route) : null;
    }

    private PrescriptionJpaEntity.PrescriptionStatusEnum mapPrescriptionStatus(PrescriptionStatus status) {
        return status != null ? PrescriptionJpaEntity.PrescriptionStatusEnum.valueOf(status.name()) : null;
    }

    private PrescriptionStatus mapPrescriptionStatusEnum(PrescriptionJpaEntity.PrescriptionStatusEnum status) {
        return status != null ? PrescriptionStatus.valueOf(status.name()) : null;
    }

    private HealthCheckupJpaEntity.CheckupTypeEnum mapCheckupType(CheckupType type) {
        return type != null ? HealthCheckupJpaEntity.CheckupTypeEnum.valueOf(type.name()) : null;
    }

    private CheckupType mapCheckupTypeEnum(HealthCheckupJpaEntity.CheckupTypeEnum type) {
        return type != null ? CheckupType.valueOf(type.name()) : null;
    }

    private HealthCheckupJpaEntity.GeneralConditionEnum mapGeneralCondition(GeneralCondition condition) {
        return condition != null ? HealthCheckupJpaEntity.GeneralConditionEnum.valueOf(condition.name()) : null;
    }

    private GeneralCondition mapGeneralConditionEnum(HealthCheckupJpaEntity.GeneralConditionEnum condition) {
        return condition != null ? GeneralCondition.valueOf(condition.name()) : null;
    }
}
