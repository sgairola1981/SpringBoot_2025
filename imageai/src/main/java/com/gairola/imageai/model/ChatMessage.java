package com.gairola.imageai.model;


public class ChatMessage {
    private String role;  // "user" or "assistant"
    private String text;  // HTML + optional IMG_URL: marker

    public ChatMessage() {}

    public ChatMessage(String role, String text) {
        this.role = role;
        this.text = text;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}