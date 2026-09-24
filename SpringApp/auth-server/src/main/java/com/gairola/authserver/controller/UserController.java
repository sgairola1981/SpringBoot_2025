package com.gairola.authserver.controller;

import com.gairola.authserver.dto.UserRequest;
import com.gairola.authserver.entity.AppUser;
import com.gairola.authserver.repository.AppUserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserController(
            AppUserRepository repository,
            PasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(
            @RequestBody UserRequest request) {
        System.out.println("----------------");

        if (repository.countByUsername(request.getUsername()) > 0) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        AppUser user = new AppUser();

        user.setUsername(request.getUsername());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(
                request.getRole() == null
                        ? "USER"
                        : request.getRole()
        );

        user.setEnabled(true);

        repository.save(user);

        return ResponseEntity.ok(
                "User created successfully"
        );
    }
}