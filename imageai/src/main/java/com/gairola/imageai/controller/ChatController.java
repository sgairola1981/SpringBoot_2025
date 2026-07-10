package com.gairola.imageai.controller;

import com.gairola.imageai.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("messages", chatService.getMessages());
        return "index";
    }

    @PostMapping("/chat")
    public String chat(@RequestParam("question") String question) {
        chatService.handleUserMessage(question);
        return "redirect:/";
    }

    @PostMapping("/clear")
    public String clear() {
        chatService.clearMessages();
        return "redirect:/";
    }
}
