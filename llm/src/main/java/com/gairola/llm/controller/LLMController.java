package com.gairola.llm.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@RestController
@RequestMapping("/api/llm")
@CrossOrigin(origins = "*")
public class LLMController {

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final String MODEL_NAME = "llama3";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public LLMController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @GetMapping(value = "/stream", produces = "text/event-stream")
    public SseEmitter stream(@RequestParam String question) {

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        new Thread(() -> {

            try {

                String requestBody = objectMapper.writeValueAsString(
                        new OllamaRequest(MODEL_NAME, question, true)
                );

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(OLLAMA_URL))
                        .timeout(Duration.ofSeconds(120))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                HttpResponse<java.io.InputStream> response =
                        httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(response.body()));

                String line;
                StringBuilder previousText = new StringBuilder();

                while ((line = reader.readLine()) != null) {

                    JsonNode jsonNode = objectMapper.readTree(line);

                    if (jsonNode.has("response")) {

                        String chunk = jsonNode.get("response").asText();

                        if (chunk != null && !chunk.isEmpty()) {

                            // Smart spacing logic
                            if (previousText.length() > 0) {

                                char lastChar = previousText.charAt(previousText.length() - 1);
                                char firstChar = chunk.charAt(0);

                              /*  if (Character.isLetterOrDigit(lastChar) &&
                                        Character.isLetterOrDigit(firstChar)) {
                                    chunk = " " + chunk;
                                }*/
                                if (!chunk.startsWith(" ") &&
                                        !chunk.startsWith("\n") &&
                                        !chunk.equals(".") &&
                                        !chunk.equals(",") &&
                                        !chunk.equals("!") &&
                                        !chunk.equals("?") &&
                                        !chunk.equals(":") &&
                                        !chunk.equals(";")) {
                                    chunk = " " + chunk;
                                  //  System.out.println("--->"+chunk);
                                }
                            }

                            previousText.append(chunk);
                           // System.out.println("--*****->"+chunk);
                            emitter.send(SseEmitter.event().data(chunk));
                        }
                    }

                    if (jsonNode.has("done") && jsonNode.get("done").asBoolean()) {
                        break;
                    }
                }

                emitter.complete();

            } catch (Exception e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }

        }).start();

        return emitter;
    }

    // DTO for Ollama request
    static class OllamaRequest {
        public String model;
        public String prompt;
        public boolean stream;

        public OllamaRequest(String model, String prompt, boolean stream) {
            this.model = model;
            this.prompt = prompt;
            this.stream = stream;
        }
    }
}