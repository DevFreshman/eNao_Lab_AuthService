package org.com.lab.repository;

import org.com.lab.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthUserJpaRepository extends JpaRepository<AuthUser, String> {
    boolean existsByUsername(String username);

    Optional<AuthUser> findByUsername(String username);
}
