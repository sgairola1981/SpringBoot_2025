package com.gairola.imageai.service;


import com.gairola.imageai.model.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    private final StableDiffusionService sdService;

    // In-memory chat history for demo
    private final List<ChatMessage> messages = new ArrayList<>();

    public ChatService(StableDiffusionService sdService) {
        this.sdService = sdService;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void clearMessages() {
        messages.clear();
    }

    public void handleUserMessage(String question) {
        messages.add(new ChatMessage("user", escapeHtml(question)));

        // If message starts with "image:", treat remaining text as image prompt
        if (question.toLowerCase().startsWith("image:")) {
            String prompt = question.substring("image:".length()).trim();
            String answer = answerWithImage(prompt);
            messages.add(new ChatMessage("assistant", answer));
        } else {
            String answer = answerTextOnly(question);
            messages.add(new ChatMessage("assistant", answer));
        }
    }

    private String answerTextOnly(String question) {
        // Simple echo logic for demo; replace with your LLM/RAG call
        StringBuilder sb = new StringBuilder();
        sb.append("<p>You said: <strong>")
                .append(escapeHtml(question))
                .append("</strong></p>");
        sb.append("<p>To generate an image, type:<br>")
                .append("<code>image: a friendly man saying hello</code></p>");
        return sb.toString();
    }

    private String answerWithImage(String imagePrompt) {
        // 1) Call SD
        String imageUrl = sdService.generateImageToFile(imagePrompt);

        // 2) Build HTML + IMG_URL marker
        StringBuilder sb = new StringBuilder();
        sb.append("<p>I generated an image for your prompt:</p>");
        sb.append("<p><em>").append(escapeHtml(imagePrompt)).append("</em></p>");
        sb.append("\nIMG_URL:").append(imageUrl);
        return sb.toString();
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
