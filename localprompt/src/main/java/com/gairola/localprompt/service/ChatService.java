package com.gairola.localprompt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;

    private static final int TIMEOUT_SECONDS = 180;

    public String answer(String question) {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> future = executor.submit(() -> {

            String response = chatClient
                    .prompt()
                    .system("""
                            You are an expert AI assistant.

                            Rules:
                            1. Give complete answers.
                            2. Never stop in the middle of a sentence.
                            3. Finish with a proper conclusion.
                            4. Use bullet points when appropriate.
                            5. If the question is technical, provide examples.
                            """)
                    .user(question)
                    .call()
                    .content();

            return response;
        });

        try {

            String result = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

            System.out.println("======================================");
            System.out.println("QUESTION = " + question);
            System.out.println("RESPONSE LENGTH = " +
                    (result != null ? result.length() : 0));
            System.out.println("ANSWER_FROM_AI = ");
            System.out.println(result);
            System.out.println("======================================");

            return result;

        } catch (TimeoutException e) {

            future.cancel(true);

            return """
                    The AI response timed out.
                    Please try again with a shorter question.
                    """;

        } catch (Exception e) {

            e.printStackTrace();

            return """
                    Sorry, an error occurred while generating the response.
                    Please try again.
                    """;

        } finally {
            executor.shutdownNow();
        }
    }
}