package com.gairola.ollama.dto;

import jakarta.validation.constraints.NotBlank;

public class ChatForm {

    @NotBlank(message = "Prompt is required")
    private String prompt;

    public ChatForm() {
    }

    public ChatForm(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}