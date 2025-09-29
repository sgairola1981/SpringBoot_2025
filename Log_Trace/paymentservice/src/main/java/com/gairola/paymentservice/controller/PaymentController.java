package com.gairola.paymentservice.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class PaymentController {

    @GetMapping("/payments")
    public List<String> getPayments() {
        return List.of("Payment1", "Payment2", "Payment3");
    }
}
