package com.gairola.llm.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RagService {

    private final VectorSearchService vectorSearchService;
    private final ChatLanguageModel chatModel;

    public String ask(String question, String sessionId) {

        // search relevant context
        String context = vectorSearchService.search(question);

        String prompt = """
                Answer the question using the context below.

                Context:
                %s

                Question:
                %s
                """.formatted(context, question);

        return chatModel.generate(prompt);
    }
    public String getContext(String question){

        return vectorSearchService.search(question);

    }
}