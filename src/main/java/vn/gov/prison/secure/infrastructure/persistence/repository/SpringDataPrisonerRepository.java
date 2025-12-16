package vn.gov.prison.secure.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.gov.prison.secure.infrastructure.persistence.entity.PrisonerJpaEntity;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for PrisonerJpaEntity
 * Provides database access methods
 */
@Repository
public interface SpringDataPrisonerRepository extends JpaRepository<PrisonerJpaEntity, String> {

    Optional<PrisonerJpaEntity> findByPrisonerNumber(String prisonerNumber);

    Optional<PrisonerJpaEntity> findByNationalIdNumber(String nationalIdNumber);

    List<PrisonerJpaEntity> findByStatus(PrisonerJpaEntity.PrisonerStatus status);

    List<PrisonerJpaEntity> findByAssignedFacility(String facilityId);

    @Query("SELECT p FROM PrisonerJpaEntity p WHERE " +
            "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) AND " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    List<PrisonerJpaEntity> searchByName(@Param("firstName") String firstName,
            @Param("lastName") String lastName);

    boolean existsByPrisonerNumber(String prisonerNumber);

    long countByStatus(PrisonerJpaEntity.PrisonerStatus status);
}
