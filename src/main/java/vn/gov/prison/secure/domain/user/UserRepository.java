package vn.gov.prison.secure.domain.user;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(UserId id);

    Optional<User> findByUsername(String username);

    Optional<User> findByFingerprintData(String fingerprintData);

    boolean existsByUsername(String username);

    void delete(UserId id);
}
