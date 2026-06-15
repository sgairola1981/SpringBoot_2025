package com.gairola.localprompt.controller;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
public class RawOllamaTestController {

    @SneakyThrows
    @GetMapping("/raw-ollama")
    public String rawOllama() {
        String body = """
            {
              "model": "mistral:latest",
              "prompt": "Say hello from raw HTTP",
              "stream": false
            }
            """;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:11434/api/generate"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("RAW_OLLAMA_STATUS = " + response.statusCode());
        System.out.println("RAW_OLLAMA_BODY = " + response.body());
        return response.body();
    }
}