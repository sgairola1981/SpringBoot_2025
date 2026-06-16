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
    private final UploadedContentRepository contentRepository;  // ✅ ADD THIS

    // ✅ Simple in-memory cache for search results
    private final Map<String, List<String>> searchCache = new ConcurrentHashMap<>();
    private static final long CACHE_TTL = 5 * 60 * 1000; // 5 minutes
    private final AtomicLong cacheLastCleanup = new AtomicLong(System.currentTimeMillis());

    private static final int TIMEOUT_SECONDS = 180;
    private static final int MAX_RETRY_ATTEMPTS = 2;
    private static final int SEARCH_LIMIT = 5; // ✅ Search top 5 documents

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public String answer(String question) {
        return answerWithRetry(question, 0);
    }

    private String answerWithRetry(String question, int attempt) {
        Future<String> future = executor.submit(() -> {
            // ✅ STEP 1: Search database for relevant content
            List<String> relevantContext = searchDatabase(question);

            System.out.println("======================================");
            System.out.println("SEARCH RESULTS FOUND: " + relevantContext.size());
            for (int i = 0; i < relevantContext.size(); i++) {
                System.out.println("DOC " + (i+1) + ": " + relevantContext.get(i).length() + " chars");
            }
            System.out.println("======================================");

            // ✅ STEP 2: Build context string for AI
            String context = buildContextString(relevantContext);

            // ✅ STEP 3: Call AI with context (RAG)
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

            // ✅ STEP 4: Convert Markdown to HTML
            Parser parser = Parser.builder().build();
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            String html = renderer.render(parser.parse(response));

            // ✅ STEP 5: Convert Unicode bullets to HTML
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

    // ✅ STEP 1: Search database with caching
    private List<String> searchDatabase(String query) {
        // ✅ Check cache first
        long now = System.currentTimeMillis();
        if (now - cacheLastCleanup.get() > CACHE_TTL) {
            searchCache.clear();
            cacheLastCleanup.set(now);
        }

        if (searchCache.containsKey(query)) {
            System.out.println("✅ CACHE HIT for query: " + query);
            return searchCache.get(query);
        }

        System.out.println("❌ CACHE MISS for query: " + query);

        // ✅ Search database (optimized query)
        List<String> results = new ArrayList<>();

        try {
            // Use native Oracle query with ROWNUM
            List<UploadedContent> docs = contentRepository.findFirstMatching(query, SEARCH_LIMIT);

            for (UploadedContent doc : docs) {
                if (doc.getContent() != null && !doc.getContent().isEmpty()) {
                    // Extract first 1000 chars to save tokens
                    String content = doc.getContent();
                    String truncated = content.length() > 1000
                            ? content.substring(0, 1000)
                            : content;

                    results.add("Source: " + doc.getFilename() + "\n" + truncated);
                }
            }

            // ✅ Cache results
            searchCache.put(query, results);
            System.out.println("✅ Cached " + results.size() + " results");

        } catch (Exception e) {
            System.out.println("❌ Database search error: " + e.getMessage());
            e.printStackTrace();
        }

        return results;
    }

    // ✅ STEP 2: Build context string
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

    // ✅ Convert Unicode bullets to HTML list
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
        if (lastOpenP > lastCloseP) {
            return true;
        }

        return false;
    }
}