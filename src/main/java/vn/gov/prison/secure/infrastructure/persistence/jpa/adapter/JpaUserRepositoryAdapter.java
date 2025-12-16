package vn.gov.prison.secure.infrastructure.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.gov.prison.secure.domain.user.*;
import vn.gov.prison.secure.infrastructure.persistence.jpa.entity.UserJpaEntity;
import vn.gov.prison.secure.infrastructure.persistence.jpa.repository.SpringDataUserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaUserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository springDataUserRepository;

    @Override
    public User save(User user) {
        UserJpaEntity entity = toEntity(user);
        UserJpaEntity saved = springDataUserRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return springDataUserRepository.findById(id.getValue())
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return springDataUserRepository.findByUsername(username)
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findByFingerprintData(String fingerprintData) {
        return springDataUserRepository.findByFingerprintData(fingerprintData)
                .map(this::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return springDataUserRepository.existsByUsername(username);
    }

    @Override
    public void delete(UserId id) {
        springDataUserRepository.deleteById(id.getValue());
    }

    private UserJpaEntity toEntity(User user) {
        return UserJpaEntity.builder()
                .id(user.getId().getValue())
                .username(user.getUsername())
                .passwordHash(user.getPasswordHash())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .userType(user.getUserType().name())
                .status(user.getStatus().name())
                .fingerprintData(user.getFingerprintData())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private User toDomain(UserJpaEntity entity) {
        return User.builder()
                .id(UserId.of(entity.getId()))
                .username(entity.getUsername())
                .passwordHash(entity.getPasswordHash())
                .email(entity.getEmail())
                .fullName(entity.getFullName())
                .userType(UserType.valueOf(entity.getUserType()))
                .status(UserStatus.valueOf(entity.getStatus()))
                .fingerprintData(entity.getFingerprintData())
                .phoneNumber(entity.getPhoneNumber())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
