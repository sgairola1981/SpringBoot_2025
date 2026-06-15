package com.gairola.localprompt.entity;

public class ChatMessage {

    private String role;   // "user" or "assistant"
    private String text;

    public ChatMessage(String role, String text) {
        this.role = role;
        this.text = text;
    }

    public String getRole() {
        return role;
    }

    public String getText() {
        return text;
    }
}