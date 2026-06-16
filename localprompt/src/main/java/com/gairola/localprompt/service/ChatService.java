package com.gairola.localprompt.service;

import lombok.RequiredArgsConstructor;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import com.gairola.localprompt.repository.UploadedContentRepository;
import com.gairola.localprompt.entity.UploadedContent;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;
    private final UploadedContentRepository contentRepository;

    private final Map<String, List<String>> searchCache = new ConcurrentHashMap<>();
    private static final long CACHE_TTL = 5 * 60 * 1000;
    private final AtomicLong cacheLastCleanup = new AtomicLong(System.currentTimeMillis());

    // ✅ EXECUTION TIME SETTINGS
    private static final int TIMEOUT_SECONDS = 180;  // Reduced from 180 to 30 seconds
    private static final int MAX_RETRY_ATTEMPTS = 2;
    private static final int SEARCH_LIMIT = 5;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public String answer(String question) {
        return answerWithRetry(question, 0);
    }

    private String answerWithRetry(String question, int attempt) {
        long startTime = System.currentTimeMillis();  // ✅ Start timing

        Future<String> future = executor.submit(() -> {
            // ✅ STEP 1: Search database for relevant content
            List<String> relevantContext = searchDatabase(question);

            System.out.println("======================================");
            System.out.println("SEARCH RESULTS FOUND: " + relevantContext.size());
            long searchTime = System.currentTimeMillis() - startTime;
            System.out.println("SEARCH TIME: " + searchTime + "ms");
            System.out.println("======================================");

            // ✅ STEP 2: Build context string
            String context = buildContextString(relevantContext);

            // ✅ STEP 3: Call AI with context (RAG)
            long aiStartTime = System.currentTimeMillis();
            System.out.println("🤖 Calling AI... Start time: " + aiStartTime);

            String response = chatClient
                    .prompt()
                    .system("""
                            You are an expert AI assistant with access to user's uploaded documents.
                            
                            Rules:
                            1. Use the provided context from uploaded documents to answer.
                            2. If context doesn't contain the answer, say "I don't have information about this in your uploaded documents."
                            3. Give complete answers.
                            4. Never stop in the middle of a sentence.
                            5. Finish with a proper conclusion.
                            6. Use HTML bullet points (<ul><li>) when appropriate, NOT Unicode •
                            7. If the question is technical, provide examples from the context.
                            8. Keep conclusions brief if approaching token limit.
                            
                            Context from uploaded documents:
                            {context}
                            """)
                    .user(question)
                    .call()
                    .content();

            long aiTime = System.currentTimeMillis() - aiStartTime;
            System.out.println("✅ AI Response time: " + aiTime + "ms");

            // ✅ STEP 4: Convert Markdown to HTML
            Parser parser = Parser.builder().build();
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            String html = renderer.render(parser.parse(response));

            // ✅ STEP 5: Convert Unicode bullets to HTML
            html = convertBulletsToList(html);

            return html;
        });

        try {
            // ✅ STOP EXECUTION if timeout exceeded
            String result = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

            long totalTime = System.currentTimeMillis() - startTime;

            System.out.println("======================================");
            System.out.println("QUESTION = " + question);
            System.out.println("TOTAL EXECUTION TIME = " + totalTime + "ms (" + (totalTime/1000.0) + " seconds)");
            System.out.println("RESPONSE LENGTH = " + (result != null ? result.length() : 0));
            System.out.println("ANSWER_FROM_AI = ");
            System.out.println(result);
            System.out.println("======================================");

            if (isResponseIncomplete(result)) {
                System.out.println("⚠️ WARNING: Response appears incomplete");

                if (attempt < MAX_RETRY_ATTEMPTS) {
                    return answerWithRetry(question, attempt + 1);
                } else {
                    return result + "<p><em>(Response was cut off due to token limit)</em></p>";
                }
            }

            return result;

        } catch (TimeoutException e) {
            future.cancel(true);  // ✅ STOP execution

            long totalTime = System.currentTimeMillis() - startTime;

            System.out.println("❌ TIMEOUT: Execution stopped after " + totalTime + "ms");
            System.out.println("TIMEOUT: Retry attempt " + (attempt + 1));

            if (attempt < MAX_RETRY_ATTEMPTS) {
                return answerWithRetry(question, attempt + 1);
            }

            return """
                    ⏱️ The AI response took too long (exceeded " + TIMEOUT_SECONDS + " seconds).
                    Please try again with a shorter question.
                    """;

        } catch (Exception e) {
            long totalTime = System.currentTimeMillis() - startTime;

            System.out.println("❌ ERROR: Execution failed after " + totalTime + "ms");
            e.printStackTrace();

            return """
                    Sorry, an error occurred while generating the response.
                    Please try again.
                    """;
        }
    }

    private List<String> searchDatabase(String query) {
        long searchStart = System.currentTimeMillis();

        long now = System.currentTimeMillis();
        if (now - cacheLastCleanup.get() > CACHE_TTL) {
            searchCache.clear();
            cacheLastCleanup.set(now);
        }

        if (searchCache.containsKey(query)) {
            System.out.println("✅ CACHE HIT for query: " + query);
            long cacheTime = System.currentTimeMillis() - searchStart;
            System.out.println("CACHE SEARCH TIME: " + cacheTime + "ms");
            return searchCache.get(query);
        }

        System.out.println("❌ CACHE MISS for query: " + query);

        List<String> results = new ArrayList<>();

        try {
            long dbStart = System.currentTimeMillis();
            List<UploadedContent> docs = contentRepository.findFirstMatching(query, SEARCH_LIMIT);
            long dbTime = System.currentTimeMillis() - dbStart;

            System.out.println("✅ Database query time: " + dbTime + "ms");
            System.out.println("✅ Found " + docs.size() + " documents");

            for (UploadedContent doc : docs) {
                if (doc.getContent() != null && !doc.getContent().isEmpty()) {
                    String content = doc.getContent();
                    String truncated = content.length() > 1000
                            ? content.substring(0, 1000)
                            : content;

                    results.add("Source: " + doc.getFilename() + "\n" + truncated);
                }
            }

            searchCache.put(query, results);

        } catch (Exception e) {
            System.out.println("❌ Database search error: " + e.getMessage());
            e.printStackTrace();
        }

        long totalTime = System.currentTimeMillis() - searchStart;
        System.out.println("📊 TOTAL SEARCH TIME: " + totalTime + "ms");

        return results;
    }

    private String buildContextString(List<String> relevantContext) {
        if (relevantContext.isEmpty()) {
            return "No relevant documents found in your uploaded content.";
        }

        StringBuilder context = new StringBuilder();
        for (int i = 0; i < relevantContext.size(); i++) {
            context.append("[").append(i + 1).append("] ").append(relevantContext.get(i)).append("\n\n");
        }
        return context.toString();
    }

    private String convertBulletsToList(String html) {
        if (html == null || html.isEmpty()) {
            return html;
        }

        String[] lines = html.split("\n");
        StringBuilder result = new StringBuilder();
        boolean inList = false;

        for (String line : lines) {
            String trimmed = line.trim();

            if (trimmed.matches("^•\\s.*") ||
                    trimmed.matches("^-\\s.*") ||
                    trimmed.matches("^*\\s.*")) {

                if (!inList) {
                    result.append("<ul>");
                    inList = true;
                }

                String text = trimmed.replaceAll("^•\\s|-\\s|*\\s", "");
                result.append("<li>").append(text).append("</li>\n");
            } else {
                if (inList) {
                    result.append("</ul>");
                    inList = false;
                }
                result.append(line).append("\n");
            }
        }

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
        return lastOpenP > lastCloseP;
    }
}