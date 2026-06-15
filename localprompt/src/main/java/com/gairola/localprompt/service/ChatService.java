package com.gairola.localprompt.service;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;
    private static final int TIMEOUT_SECONDS = 181;

    public String answer(String question) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() ->
                chatClient
                        .prompt()
                        .user("Answer briefly: " + question)
                        .call()
                        .content()
        );

        try {
            String result = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            System.out.println("ANSWER_FROM_AI = [" + result + "]");
            return result;
        } catch (TimeoutException e) {
            future.cancel(true);
            System.out.println("AI CALL TIMED OUT");
            return "AI call timed out after " + TIMEOUT_SECONDS + " seconds.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, I could not get an answer from the AI service. Error: " + e.getMessage();
        } finally {
            executor.shutdownNow();
        }
    }
}