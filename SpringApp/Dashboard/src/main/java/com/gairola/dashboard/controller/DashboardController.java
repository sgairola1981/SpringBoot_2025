package com.gairola.dashboard.controller;

import com.gairola.dashboard.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class DashboardController {

    private final AuthService authService;

    public DashboardController(AuthService authService) {
        this.authService = authService;
    }

    // ===============================
    // HOME
    // ===============================

    @GetMapping("/")
    public String home(HttpSession session) {

        if (session.getAttribute("USERNAME") != null) {
            return "dashboard";
        }

        return "login";
    }

    // ===============================
    // LOGIN PAGE
    // ===============================

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // ===============================
    // LOGIN
    // ===============================

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        try {

            Map<String, Object> response =
                    authService.login(username, password);

            String token =
                    (String) response.get("token");

            String loggedUsername =
                    (String) response.get("username");

            String role =
                    (String) response.get("role");

            // Store JWT
            session.setAttribute(
                    "JWT_TOKEN",
                    token
            );

            // Store username
            session.setAttribute(
                    "USERNAME",
                    loggedUsername
            );

            // Store role
            session.setAttribute(
                    "ROLE",
                    role
            );

            return "redirect:/dashboard";

        } catch (Exception e) {

            model.addAttribute(
                    "error",
                    "Invalid username or password"
            );

            return "login";
        }
    }

    // ===============================
    // DASHBOARD
    // ===============================

    @GetMapping("/dashboard")
    public String dashboard(
            HttpSession session,
            Model model) {

        String username =
                (String) session.getAttribute("USERNAME");

        if (username == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "username",
                username
        );

        model.addAttribute(
                "role",
                session.getAttribute("ROLE")
        );

        return "dashboard";
    }

    // ===============================
    // LOGOUT
    // ===============================

    @PostMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login?logout=true";
    }
}