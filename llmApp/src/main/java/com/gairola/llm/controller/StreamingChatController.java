package com.gairola.llm.controller;

import com.gairola.llm.service.StreamingAIService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/ai")
public class StreamingChatController {

    private final StreamingAIService aiService;

    public StreamingChatController(StreamingAIService aiService) {
        this.aiService = aiService;
    }

    @GetMapping(value = "/stream", produces = "text/event-stream")
    public SseEmitter stream(@RequestParam String prompt) {

        SseEmitter emitter = new SseEmitter();

        aiService.streamAnswer(prompt, token -> {
            try {
                emitter.send(token);
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }
}