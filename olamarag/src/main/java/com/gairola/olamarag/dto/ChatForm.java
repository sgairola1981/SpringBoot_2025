package com.gairola.olamarag.dto;

import jakarta.validation.constraints.NotBlank;

public class ChatForm {

    @NotBlank(message = "Prompt is required")
    private String prompt;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}