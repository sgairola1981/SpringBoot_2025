package com.vayam.Clenetapp.controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {

    private final WebClient webClient;

    public UserController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @GetMapping("/{userId}")
    public String getUserDetails(@PathVariable String userId, Model model) {
        String apiUrl = "http://localhost:8888/api/user-details/" + userId;

        Map<String, Object> response = (Map<String, Object>) webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {})
                .block(); // Blocking for Thymeleaf rendering

        model.addAttribute("user", response.get("user"));
        model.addAttribute("orders", response.get("orders"));
        return "user-details";
    }
}
