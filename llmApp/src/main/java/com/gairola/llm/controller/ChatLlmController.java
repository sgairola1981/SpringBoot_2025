package com.gairola.llm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatLlmController {

    @GetMapping("/")
    public String home() {
        return "chat";
    }
}