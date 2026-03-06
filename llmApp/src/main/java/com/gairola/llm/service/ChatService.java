package com.gairola.llm.service;

import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final RagService ragService;

    public ChatService(RagService ragService){
        this.ragService = ragService;
    }

    public String chat(String question){

        String context="Search context from DB";

        return ragService.ask(context,question);

    }

}