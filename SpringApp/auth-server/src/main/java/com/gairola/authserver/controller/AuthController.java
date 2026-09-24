package com.gairola.authserver.controller;

import com.gairola.authserver.dto.LoginRequest;
import com.gairola.authserver.dto.LoginResponse;
import com.gairola.authserver.entity.AppUser;
import com.gairola.authserver.repository.AppUserRepository;
import com.gairola.authserver.service.JwtService;

import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final AppUserRepository repository;

    private final JwtService jwtService;

    public AuthController(
            AuthenticationManager authenticationManager,
            AppUserRepository repository,
            JwtService jwtService) {

        this.authenticationManager =
                authenticationManager;

        this.repository = repository;

        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            AppUser user =
                    repository.findByUsername(
                            request.getUsername()
                    ).orElseThrow();

            String token =
                    jwtService.generateToken(
                            user.getUsername(),
                            user.getRole()
                    );

            return ResponseEntity.ok(
                    new LoginResponse(
                            token,
                            user.getUsername(),
                            user.getRole()
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(
                            HttpStatus.UNAUTHORIZED
                    )
                    .body(
                            "Invalid username or password"
                    );
        }
    }
}