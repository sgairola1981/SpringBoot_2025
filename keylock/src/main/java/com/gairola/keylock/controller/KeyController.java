package com.gairola.keylock.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

@RestController
public class KeyController {
    @Operation(summary = "URL Mapping")
    @GetMapping("/public/hello")
    public String publicHello() {
        return "Hello from public!";
    }

    @GetMapping("/user/hello")
    public String userHello(@AuthenticationPrincipal Jwt jwt) {
        return "Hello USER, " + jwt.getClaimAsString("dsdd");
    }

    @GetMapping("/admin/hello")
    public String adminHello(@AuthenticationPrincipal Jwt jwt) {
        return "Hello ADMIN, " + jwt.getClaimAsString("admin");
    }
}