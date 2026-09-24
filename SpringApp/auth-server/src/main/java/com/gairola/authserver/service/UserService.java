package com.gairola.authserver.service;

import com.gairola.authserver.entity.AppUser;
import com.gairola.authserver.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            AppUserRepository repository,
            PasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    // ============================
    // LIST
    // ============================

    public List<AppUser> findAll() {
        return repository.findAllUsers();
    }

    // ============================
    // FIND
    // ============================

    public AppUser findById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found: " + id
                        ));
    }

    // ============================
    // CREATE
    // ============================

    public void createUser(
            String username,
            String password,
            String role) {

        if (username == null || username.isBlank()) {
            throw new RuntimeException(
                    "Username is required"
            );
        }

        if (password == null || password.isBlank()) {
            throw new RuntimeException(
                    "Password is required"
            );
        }

        if (repository.countByUsername(username) > 0) {
            throw new RuntimeException(
                    "Username already exists"
            );
        }

        AppUser user = new AppUser();

        user.setUsername(username.trim());

        // BCrypt
        user.setPassword(
                passwordEncoder.encode(password)
        );

        user.setRole(
                role == null || role.isBlank()
                        ? "USER"
                        : role
        );

        user.setEnabled(true);

        repository.save(user);
    }

    // ============================
    // UPDATE
    // ============================

    public void updateUser(
            Long id,
            String username,
            String password,
            String role,
            Boolean enabled) {

        AppUser user = findById(id);

        if (username == null || username.isBlank()) {
            throw new RuntimeException(
                    "Username is required"
            );
        }

        username = username.trim();

        // Check duplicate username
        if (!user.getUsername().equals(username)
                && repository.countByUsername(username) > 0) {

            throw new RuntimeException(
                    "Username already exists"
            );
        }

        user.setUsername(username);

        /*
         * Password is optional during UPDATE.
         *
         * Blank password =
         * keep existing password.
         */
        if (password != null
                && !password.isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(password)
            );
        }

        user.setRole(
                role == null || role.isBlank()
                        ? "USER"
                        : role
        );

        user.setEnabled(
                enabled != null && enabled
        );

        repository.save(user);
    }

    // ============================
    // DELETE
    // ============================

    public void deleteUser(Long id) {

        findById(id);

        repository.deleteById(id);
    }
}