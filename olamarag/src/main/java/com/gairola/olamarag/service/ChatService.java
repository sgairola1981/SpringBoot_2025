package com.gairola.olamarag.service;

import com.gairola.olamarag.service.KnowledgeService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final KnowledgeService knowledgeService;

    public ChatService(ChatClient.Builder chatClientBuilder,
                       VectorStore vectorStore,
                       KnowledgeService knowledgeService) {
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
        this.knowledgeService = knowledgeService;
    }

    public String ask(String prompt) {
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        .topK(3)
                        .build())
                .build();

        String response = chatClient.prompt()
                .advisors(advisor)
                .user(prompt)
                .call()
                .content();

        knowledgeService.saveKnowledge(prompt, response);
        return response;
    }
}