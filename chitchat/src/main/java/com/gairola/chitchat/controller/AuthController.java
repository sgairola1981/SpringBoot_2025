package com.gairola.chitchat.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username, HttpSession session) {
        session.setAttribute("username", username);
        System.out.println("User Name:  using   login/" + username);
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
        System.out.println("User Name:  using   chat/" + username);
        if (username == null || username.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("username", username);
        return "chat";
    }
}
