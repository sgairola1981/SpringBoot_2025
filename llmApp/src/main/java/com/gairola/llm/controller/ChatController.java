package com.gairola.llm.controller;

import com.gairola.llm.service.RagService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class ChatController {

    private final RagService ragService;

    public ChatController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/ask")
    public SseEmitter ask(@RequestParam String q){

        SseEmitter emitter = new SseEmitter();

        new Thread(() -> {

            try{
                String sessionId = "default-session";
                String answer = ragService.ask(q,sessionId);

                emitter.send(answer);

                emitter.complete();

            }catch(Exception e){
                emitter.completeWithError(e);
            }

        }).start();

        return emitter;
    }
}