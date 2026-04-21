package com.gairola.aidemo.controller;

import com.gairola.aidemo.entity.User;
import com.gairola.aidemo.repository.UserRepository;
import com.gairola.aidemo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin("*") // allow frontend
public class AuthController {

    private final UserRepository repo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        // check if user exists
        if (repo.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Username already exists"));
        }

        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);

        return ResponseEntity.ok(
                Map.of("message", "User registered successfully")
        );
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        User dbUser = repo.findByUsername(user.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (!encoder.matches(user.getPassword(), dbUser.getPassword())) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of("error", "Invalid password"));
        }

        String token = jwtUtil.generateToken(dbUser.getUsername());

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "username", dbUser.getUsername()
                )
        );
    }
}