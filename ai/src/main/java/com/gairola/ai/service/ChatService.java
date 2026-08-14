package com.gairola.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String queryAi(String promptText) {
        return chatClient
                .prompt()
                .user(promptText)
                .call()
                .content();
    }

    public String getCityGuide(String city, String interest) {
        return chatClient
                .prompt()
                .user(user -> user
                        .text("""
                            I am a tourist visiting the city of {city}.
                            I am mostly interested in {interest}.
                            Tell me tips on what to do there.
                            """)
                        .param("city", city)
                        .param("interest", interest))
                .call()
                .content();
    }
}