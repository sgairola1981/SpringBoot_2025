package com.gairola.authserver.repository;

import com.gairola.authserver.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppUserRepository
        extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);
    @Query(value = """
            SELECT COUNT(*)
            FROM APP_USERS
            WHERE USERNAME = :username
            """,
            nativeQuery = true)
    long countByUsername(
            @Param("username") String username
    );
}