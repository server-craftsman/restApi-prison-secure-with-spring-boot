package vn.gov.prison.secure.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.gov.prison.secure.infrastructure.persistence.jpa.entity.EscapeRequestJpaEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataEscapeRequestRepository extends JpaRepository<EscapeRequestJpaEntity, UUID> {

    List<EscapeRequestJpaEntity> findByPrisonerId(String prisonerId);

    List<EscapeRequestJpaEntity> findByStatus(String status);
}
