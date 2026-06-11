package com.gairola.ollama.controller;

import com.gairola.ollama.dto.ChatForm;
import com.gairola.ollama.entity.KnowledgeItem;
import com.gairola.ollama.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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
            String prompt = chatForm.getPrompt().trim();
            List<KnowledgeItem> searchResults = chatService.searchKnowledge(prompt);
            String response = chatService.getAnswer(prompt);
            long responseTimeMs = System.currentTimeMillis() - start;

            model.addAttribute("chatForm", chatForm);
            model.addAttribute("searchResults", searchResults);
            model.addAttribute("response", response);
            model.addAttribute("responseTimeMs", responseTimeMs);

            if (searchResults.isEmpty()) {
                model.addAttribute("infoMessage", "Data not found in database, so AI response was generated and stored.");
            } else {
                model.addAttribute("infoMessage", "Answer returned directly from database.");
            }

        } catch (Exception e) {
            model.addAttribute("chatForm", chatForm);
            model.addAttribute("errorMessage", "Error while processing request: " + e.getMessage());
        }

        return "index";
    }
}