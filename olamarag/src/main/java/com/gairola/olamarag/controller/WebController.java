package com.gairola.olamarag.controller;

import com.gairola.olamarag.dto.ChatForm;
import com.gairola.olamarag.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {

    private final ChatService chatService;

    public WebController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/")
    public String home(Model model) {
        if (!model.containsAttribute("chatForm")) {
            model.addAttribute("chatForm", new ChatForm());
        }
        return "index";
    }

    @PostMapping("/ask")
    public String ask(@Valid @ModelAttribute("chatForm") ChatForm chatForm,
                      BindingResult bindingResult,
                      Model model) {

        if (bindingResult.hasErrors()) {
            return "index";
        }

        long start = System.currentTimeMillis();

        try {
            String response = chatService.ask(chatForm.getPrompt().trim());
            long responseTimeMs = System.currentTimeMillis() - start;

            model.addAttribute("chatForm", chatForm);
            model.addAttribute("response", response);
            model.addAttribute("responseTimeMs", responseTimeMs);
        } catch (Exception e) {
            model.addAttribute("chatForm", chatForm);
            model.addAttribute("errorMessage", "Error while processing request: " + e.getMessage());
        }

        return "index";
    }
}