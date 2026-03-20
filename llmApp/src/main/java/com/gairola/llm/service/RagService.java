package com.gairola.llm.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RagService {

    private final VectorSearchService vectorSearchService;
    private final ChatLanguageModel chatModel;
    private final MemoryService memoryService;

    public String ask(String question, String sessionId) {

        // 1️⃣ Search context
        String context = vectorSearchService.search(question);
        System.out.println("📄 Context => " + context);

 /*       if (context == null || context.isBlank()) {
            return """
I couldn’t find relevant information in the knowledge base.

Try:
• Uploading documents
• Asking more specific questions
• Checking document content
""";
        }
*/
        // 2️⃣ Conversation memory
        String conversation = memoryService.getConversation(sessionId);

        // 3️⃣ Prompt
        String prompt = """
You are an expert knowledge assistant.

Conversation History:
%s

Context:
%s

User Question:
%s

Instructions:
• Answer ONLY using the context
• If answer not found → say clearly
• Use bullet points when helpful
• Do NOT invent information

Answer:
""".formatted(conversation, context, question);

        // 4️⃣ Generate AI response
        String answer = chatModel.generate(prompt);

        // 5️⃣ Save memory
        memoryService.append(sessionId, "User", question);
        memoryService.append(sessionId, "AI", answer);

        return answer;
    }
}