package com.example.finance_agent.controller;

import com.example.finance_agent.dto.FinanceResponse;
import com.example.finance_agent.dto.ChatRequest;
import com.example.finance_agent.service.FinanceInvestigationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

  private final FinanceInvestigationService service;

  public ChatController(FinanceInvestigationService service) {
    this.service = service;
  }

  @PostMapping
  public FinanceResponse chat(@Valid @RequestBody ChatRequest request) {
        return service.investigateWithHistory(
            request.getQuery(),
            request.getSessionId(),
            request.getHistory()
    );


  }
}