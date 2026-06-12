package com.gairola.localprompt.service;

import com.gairola.localprompt.entity.UploadedContent;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final FileService fileService;
    private final ChatClient chatClient;

    public String answer(String question) {
        try {
            String context = fileService.all().stream()
                    .map(this::formatContent)
                    .collect(Collectors.joining("\n\n"));

            String prompt = """
                Answer the question using only the context below.
                If the answer is not in the context, say: "I don't have enough information."

                CONTEXT:
                %s

                QUESTION:
                %s
                """.formatted(context, question);

            return chatClient
                    .prompt()
                    .user(prompt)
                    .call()
                    .content();
        } catch (Exception e) {
            e.printStackTrace(); // so you see the real cause in console
            return "Sorry, I could not get an answer from the AI service. Error: " + e.getMessage();
        }
    }

    private String formatContent(UploadedContent c) {
        return "SOURCE: " + c.getFilename() + "\n" + c.getContent();
    }
}