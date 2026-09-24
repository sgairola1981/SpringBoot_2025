package com.gairola.authserver.controller;

import com.gairola.authserver.dto.UserRequest;
import com.gairola.authserver.service.UserService;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(
            UserService userService) {

        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(
            @RequestBody UserRequest request) {

        try {

            userService.createUser(
                    request.getUsername(),
                    request.getPassword(),
                    request.getRole()
            );

            return ResponseEntity.ok(
                    "User created successfully"
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }
}