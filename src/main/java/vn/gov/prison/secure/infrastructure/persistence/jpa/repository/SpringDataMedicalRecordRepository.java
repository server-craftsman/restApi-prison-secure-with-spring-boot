package vn.gov.prison.secure.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.gov.prison.secure.infrastructure.persistence.jpa.entity.MedicalRecordJpaEntity;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for Medical Records
 */
@Repository
public interface SpringDataMedicalRecordRepository extends JpaRepository<MedicalRecordJpaEntity, UUID> {

    /**
     * Find all medical records for a prisoner
     */
    List<MedicalRecordJpaEntity> findByPrisonerId(String prisonerId);

    /**
     * Find medical records by prisoner and record type
     */
    List<MedicalRecordJpaEntity> findByPrisonerIdAndRecordType(
            String prisonerId,
            MedicalRecordJpaEntity.RecordTypeEnum recordType);

    /**
     * Find medical records requiring follow-up
     */
    @Query("SELECT m FROM MedicalRecordJpaEntity m WHERE m.followUpRequired = true AND m.followUpDate IS NOT NULL")
    List<MedicalRecordJpaEntity> findRecordsRequiringFollowUp();

    /**
     * Find medical records by severity
     */
    List<MedicalRecordJpaEntity> findBySeverity(MedicalRecordJpaEntity.SeverityEnum severity);

    /**
     * Find urgent medical records (HIGH or CRITICAL severity)
     */
    @Query("SELECT m FROM MedicalRecordJpaEntity m WHERE m.severity IN ('HIGH', 'CRITICAL') ORDER BY m.recordDate DESC")
    List<MedicalRecordJpaEntity> findUrgentMedicalRecords();
}
