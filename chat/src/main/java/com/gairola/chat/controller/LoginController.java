package com.gairola.chat.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, HttpSession session) {
        session.setAttribute("username", username);
        return "redirect:/chat";
    }
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate the current session
        return "redirect:/";  // Redirect back to login page
    }

    @GetMapping("/chat")
    public String chatPage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");

        if (username == null || username.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("username", username);
        return "chat";
    }
}
