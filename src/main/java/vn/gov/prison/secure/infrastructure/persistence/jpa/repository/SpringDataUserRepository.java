package vn.gov.prison.secure.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.gov.prison.secure.infrastructure.persistence.jpa.entity.UserJpaEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByUsername(String username);

    Optional<UserJpaEntity> findByFingerprintData(String fingerprintData);

    boolean existsByUsername(String username);
}
