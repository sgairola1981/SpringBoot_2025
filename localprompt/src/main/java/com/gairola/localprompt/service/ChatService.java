package com.gairola.localprompt.service;

import lombok.RequiredArgsConstructor;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import java.util.concurrent.*;
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;

    private static final int TIMEOUT_SECONDS = 180;
    private static final int MAX_RETRY_ATTEMPTS = 2;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public String answer(String question) {
        return answerWithRetry(question, 0);
    }

    private String answerWithRetry(String question, int attempt) {
        Future<String> future = executor.submit(() -> {
            String response = chatClient
                    .prompt()
                    .system("""
                            You are an expert AI assistant.

                            Rules:
                            1. Give complete answers.
                            2. Never stop in the middle of a sentence.
                            3. Finish with a proper conclusion.
                            4. Use HTML bullet points (<ul><li>) when appropriate, NOT Unicode •
                            5. If the question is technical, provide examples.
                            6. Keep conclusions brief if approaching token limit.
                            """)
                    .user(question)
                    .call()
                    .content();

            // Convert Markdown to HTML
            Parser parser = Parser.builder().build();
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            String html = renderer.render(parser.parse(response));

            // ✅ Convert Unicode bullets (•) to HTML <li> tags
            html = convertBulletsToList(html);

            return html;
        });

        try {
            String result = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

            System.out.println("======================================");
            System.out.println("QUESTION = " + question);
            System.out.println("RESPONSE LENGTH = " + (result != null ? result.length() : 0));
            System.out.println("ANSWER_FROM_AI = ");
            System.out.println(result);
            System.out.println("======================================");

            if (isResponseIncomplete(result)) {
                System.out.println("WARNING: Response appears incomplete, retrying...");
                future.cancel(true);

                if (attempt < MAX_RETRY_ATTEMPTS) {
                    return answerWithRetry(question, attempt + 1);
                } else {
                    return result + "<p><em>(Response was cut off due to token limit)</em></p>";
                }
            }

            return result;

        } catch (TimeoutException e) {
            future.cancel(true);

            if (attempt < MAX_RETRY_ATTEMPTS) {
                System.out.println("TIMEOUT: Retry attempt " + (attempt + 1));
                return answerWithRetry(question, attempt + 1);
            }

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
        }
    }

    // ✅ Convert Unicode bullets to HTML list
    private String convertBulletsToList(String html) {
        if (html == null || html.isEmpty()) {
            return html;
        }

        // Split by lines
        String[] lines = html.split("\n");
        StringBuilder result = new StringBuilder();
        boolean inList = false;

        for (String line : lines) {
            String trimmed = line.trim();

            // Check if line starts with bullet (•, -, *)
            if (trimmed.matches("^•\\s.*") ||
                    trimmed.matches("^-\\s.*") ||
                    trimmed.matches("^*\\s.*")) {

                if (!inList) {
                    result.append("<ul>");
                    inList = true;
                }

                // Extract text after bullet
                String text = trimmed.replaceAll("^•\\s|-\\s|*\\s", "");
                result.append("<li>").append(text).append("</li>\n");
            } else {
                // End list if we were in one
                if (inList) {
                    result.append("</ul>");
                    inList = false;
                }
                result.append(line).append("\n");
            }
        }

        // Close any open list
        if (inList) {
            result.append("</ul>");
        }

        return result.toString();
    }

    private boolean isResponseIncomplete(String html) {
        if (html == null || html.trim().isEmpty()) {
            return true;
        }

        String trimmed = html.trim();

        if (!trimmed.endsWith(">")) {
            return true;
        }

        if (trimmed.lastIndexOf('<') > trimmed.lastIndexOf('>')) {
            return true;
        }

        String lower = trimmed.toLowerCase();
        if (lower.matches(".*with its large$.*") ||
                lower.matches(".*with its $.*") ||
                lower.matches(".*is a $.*") ||
                lower.matches(".*it is $.*") ||
                lower.matches(".*and $.*") ||
                lower.matches(".*but $.*")) {
            return true;
        }

        int lastOpenP = trimmed.lastIndexOf("<p");
        int lastCloseP = trimmed.lastIndexOf("</p>");
        if (lastOpenP > lastCloseP) {
            return true;
        }

        return false;
    }
}