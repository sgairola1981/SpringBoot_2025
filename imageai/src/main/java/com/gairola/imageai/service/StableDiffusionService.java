package com.gairola.imageai.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;

@Service
public class StableDiffusionService {

    private final WebClient webClient;

    public StableDiffusionService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:5000") // your Stable Diffusion API URL
                .build();
    }

    /**
     * Calls Stable Diffusion API, saves PNG to disk, returns URL like /sd-images/xxx.png
     */
    public String generateImageToFile(String prompt) {
        Map<String, Object> body = Map.of(
                "prompt", prompt,
                "width", 768,
                "height", 512,
                "steps", 25
        );

        Map<String, Object> resp = webClient.post()
                .uri("/v1/generate")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        if (resp == null || !resp.containsKey("image")) {
            throw new IllegalStateException("No image returned from Stable Diffusion");
        }

        String b64 = (String) resp.get("image");
        byte[] png = Base64.getDecoder().decode(b64);

        try {
            Path dir = Paths.get("uploads/sd");
            Files.createDirectories(dir);

            String fileName = "sd_" + System.currentTimeMillis() + ".png";
            Path target = dir.resolve(fileName);
            Files.write(target, png);

            // URL to be used in the browser
            return "/sd-images/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to save SD image", e);
        }
    }
}