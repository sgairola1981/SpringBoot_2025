package com.gairola.Idempotency.controller;


import com.gairola.Idempotency.model.PaymentRequest;
import com.gairola.Idempotency.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/")
    public String showForm(Model model) {
        PaymentRequest request = new PaymentRequest();
        request.setIdempotencyKey(UUID.randomUUID().toString()); // Auto-generate key
        model.addAttribute("paymentRequest", request);
        return "payment";
    }

    @PostMapping("/submit")
    public String submitPayment(@ModelAttribute PaymentRequest paymentRequest, Model model) {
        String result = paymentService.processPayment(paymentRequest);
        model.addAttribute("message", result);
        return "payment";
    }
}
