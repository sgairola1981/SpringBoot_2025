package com.gairola.ollama.service;

import com.gairola.ollama.entity.KnowledgeItem;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final KnowledgeService knowledgeService;

    public ChatService(ChatClient.Builder chatClientBuilder, KnowledgeService knowledgeService) {
        this.chatClient = chatClientBuilder.build();
        this.knowledgeService = knowledgeService;
    }

    public List<KnowledgeItem> searchKnowledge(String prompt) {
        return knowledgeService.search(prompt);
    }

    public String getAnswer(String prompt) {
        List<KnowledgeItem> matches = knowledgeService.search(prompt);

        // If found in DB, return DB content directly
        if (!matches.isEmpty()) {
            return matches.stream()
                    .map(KnowledgeItem::getContent)
                    .collect(Collectors.joining("\n\n"));
        }

        // If not found, call Ollama
        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        // Save new content into DB
        String title = prompt.length() > 200 ? prompt.substring(0, 200) : prompt;
        String content = """
                Question:
                %s

                Answer:
                %s
                """.formatted(prompt, response);

        knowledgeService.add(title, content);

        return response;
    }
}