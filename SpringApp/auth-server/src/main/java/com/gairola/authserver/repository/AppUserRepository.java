package com.gairola.authserver.repository;

import com.gairola.authserver.entity.AppUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository
        extends JpaRepository<AppUser, Long> {

    @Query(
            value = """
                SELECT COUNT(*)
                FROM APP_USERS
                WHERE USERNAME = :username
                """,
            nativeQuery = true
    )
    long countByUsername(
            @Param("username") String username
    );

    @Query(
            value = """
                SELECT *
                FROM APP_USERS
                WHERE USERNAME = :username
                """,
            nativeQuery = true
    )
    Optional<AppUser> findByUsername(
            @Param("username") String username
    );

    @Query(
            value = """
                SELECT *
                FROM APP_USERS
                ORDER BY ID
                """,
            nativeQuery = true
    )
    List<AppUser> findAllUsers();
}