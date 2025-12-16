package vn.gov.prison.secure.domain.repository;

import vn.gov.prison.secure.domain.model.medical.MedicalRecord;
import vn.gov.prison.secure.domain.model.medical.MedicalRecordId;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Medical Records
 * Following DDD principles - domain repository
 */
public interface MedicalRecordRepository {

    /**
     * Save a medical record
     */
    MedicalRecord save(MedicalRecord medicalRecord);

    /**
     * Find medical record by ID
     */
    Optional<MedicalRecord> findById(MedicalRecordId id);

    /**
     * Find all medical records for a prisoner
     */
    List<MedicalRecord> findByPrisonerId(PrisonerId prisonerId);

    /**
     * Find medical records by prisoner and record type
     */
    List<MedicalRecord> findByPrisonerIdAndRecordType(PrisonerId prisonerId, String recordType);

    /**
     * Find medical records requiring follow-up
     */
    List<MedicalRecord> findRecordsRequiringFollowUp();

    /**
     * Delete a medical record
     */
    void delete(MedicalRecordId id);

    /**
     * Check if medical record exists
     */
    boolean existsById(MedicalRecordId id);
}
