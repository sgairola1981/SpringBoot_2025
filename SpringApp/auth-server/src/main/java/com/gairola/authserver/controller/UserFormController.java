package com.gairola.authserver.controller;

import com.gairola.authserver.entity.AppUser;
import com.gairola.authserver.repository.AppUserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserFormController {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserFormController(
            AppUserRepository repository,
            PasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/create")
    public String showCreateUserForm() {

        return "user-create";
    }

    @PostMapping("/create")
    public String createUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String role,
            Model model) {

        System.out.println("Creating user: " + username);

        if (repository.countByUsername(username) > 0) {

            model.addAttribute(
                    "error",
                    "Username already exists"
            );

            return "user-create";
        }

        AppUser user = new AppUser();

        user.setUsername(username);

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

        model.addAttribute(
                "success",
                "User created successfully"
        );

        return "user-create";
    }
}