package com.gairola.aidemo.repository;

import com.gairola.aidemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    // Optional future use
    Optional<User> findByUsernameIgnoreCase(String username);
}