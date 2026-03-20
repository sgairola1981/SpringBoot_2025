package com.gairola.llm.controller;

import com.gairola.llm.service.RagService;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class ChatController {

    private final RagService ragService;

    public ChatController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/ask")
    public SseEmitter ask(@RequestParam String q, HttpSession session){
        String sessionId = session.getId();
        SseEmitter emitter = new SseEmitter();

        new Thread(() -> {

            try{

                String answer = ragService.ask(q,sessionId);

                System.out.println("🤖 Answer ===> " + answer);
               emitter.send(answer);

                emitter.complete();

            }catch(Exception e){
                emitter.completeWithError(e);
            }

        }).start();

        return emitter;
    }


}