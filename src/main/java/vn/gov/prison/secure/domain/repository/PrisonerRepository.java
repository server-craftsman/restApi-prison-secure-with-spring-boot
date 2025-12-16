package vn.gov.prison.secure.domain.repository;

import vn.gov.prison.secure.domain.model.prisoner.Prisoner;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Prisoner aggregate
 * Following Repository Pattern and DIP (Dependency Inversion Principle)
 * 
 * Domain layer defines the interface
 * Infrastructure layer provides the implementation
 */
public interface PrisonerRepository {

    /**
     * Save a prisoner (create or update)
     */
    Prisoner save(Prisoner prisoner);

    /**
     * Find prisoner by ID
     */
    Optional<Prisoner> findById(PrisonerId id);

    /**
     * Find prisoner by prisoner number
     */
    Optional<Prisoner> findByPrisonerNumber(String prisonerNumber);

    /**
     * Find prisoner by national ID number
     */
    Optional<Prisoner> findByNationalIdNumber(String nationalIdNumber);

    /**
     * Find all prisoners by status
     */
    List<Prisoner> findByStatus(PrisonerStatus status);

    /**
     * Find prisoners by facility
     */
    List<Prisoner> findByFacility(String facilityId);

    /**
     * Find prisoners by name (partial match)
     */
    List<Prisoner> searchByName(String firstName, String lastName);

    /**
     * Find all prisoners with pagination
     */
    List<Prisoner> findAll(int page, int size);

    /**
     * Delete prisoner (soft delete recommended)
     */
    void delete(PrisonerId id);

    /**
     * Check if prisoner exists by prisoner number
     */
    boolean existsByPrisonerNumber(String prisonerNumber);

    /**
     * Count total prisoners
     */
    long count();

    /**
     * Count prisoners by status
     */
    long countByStatus(PrisonerStatus status);
}
