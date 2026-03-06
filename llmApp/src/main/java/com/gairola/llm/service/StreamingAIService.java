package com.gairola.llm.service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class StreamingAIService {

    private final OllamaStreamingChatModel model;

    public StreamingAIService() {

        this.model = OllamaStreamingChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("llama3")
                .build();
    }

    public void streamAnswer(String prompt, Consumer<String> onToken) {

        model.generate(prompt, new StreamingResponseHandler<AiMessage>() {

            @Override
            public void onNext(String token) {
                onToken.accept(token);
            }


            public void onComplete(AiMessage response) {
                System.out.println("\nCompleted");
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }
}