package com.gairola.llm.controller;
import com.gairola.llm.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DocumentController {

    private final EmbeddingService embeddingService;

    @PostMapping("/api/store")
    public String store(@RequestBody String text){

        embeddingService.storeDocument(text);

        return "Document stored successfully!";
    }
}