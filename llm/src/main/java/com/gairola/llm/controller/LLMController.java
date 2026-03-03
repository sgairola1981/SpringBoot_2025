package com.gairola.llm.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin("*")
public class LLMController {

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Global memory (for demo)
    private final List<Map<String, String>> memory = new ArrayList<>();

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody Map<String, String> request) {

        String userMessage = request.get("message");

        memory.add(Map.of(
                "role", "user",
                "content", userMessage
        ));

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama3");
        body.put("messages", memory);
        body.put("stream", true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:11434/api/chat",
                HttpMethod.POST,
                entity,
                String.class
        );

        StringBuilder result = new StringBuilder();

        try {
            BufferedReader reader =
                    new BufferedReader(new StringReader(response.getBody()));

            String line;
            while ((line = reader.readLine()) != null) {

                if (!line.isEmpty()) {
                    JsonNode node = objectMapper.readTree(line);

                    if (node.has("message")) {
                        result.append(
                                node.get("message")
                                        .get("content")
                                        .asText());   // 🚨 NO TRIM HERE
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        memory.add(Map.of(
                "role", "assistant",
                "content", result.toString()
        ));

        return ResponseEntity.ok(result.toString());
    }
}