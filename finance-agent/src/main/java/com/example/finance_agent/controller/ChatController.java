package com.example.finance_agent.controller;

import com.example.finance_agent.service.AgentService;
import com.example.finance_agent.service.FinanceInvestigationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

  //  private final AgentService service;
    private final FinanceInvestigationService service;
      public ChatController(FinanceInvestigationService service){

        this.service=service;

    }

    @PostMapping

    public String chat(@RequestBody String message){

        return service.investigate(message);

    }

}