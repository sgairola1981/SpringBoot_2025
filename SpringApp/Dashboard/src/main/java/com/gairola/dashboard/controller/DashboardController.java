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

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session) {

        try {

            // Authentication is performed by AUTH SERVER
            Map<String, Object> result =
                    authService.login(username, password);

            String token =
                    (String) result.get("token");

            String loginUsername =
                    (String) result.get("username");

            String role =
                    (String) result.get("role");

            // Store authentication information
            // in Dashboard server session
            session.setAttribute("JWT_TOKEN", token);
            session.setAttribute("USERNAME", loginUsername);
            session.setAttribute("ROLE", role);

            // Go to welcome page
            return "redirect:/dashboard";

        } catch (Exception e) {

            System.out.println(
                    "Authentication failed: "
                            + e.getMessage()
            );

            return "redirect:/login?error=true";
        }
    }

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

    @PostMapping("/logout")
    public String logout(
            HttpSession session) {

        session.invalidate();

        return "redirect:/login?logout=true";
    }
}