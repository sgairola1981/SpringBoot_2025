package com.example.payments;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final AtomicLong sequence = new AtomicLong(5000);

    @PostMapping
    public Map<String, Object> create(@RequestBody PaymentRequest request) {
        if (request.amount() == null || request.amount().signum() <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        return Map.of("paymentId", sequence.incrementAndGet(),
                      "customerName", request.customerName() == null ? "" : request.customerName(),
                      "amount", request.amount(),
                      "status", "APPROVED_DEMO",
                      "processedAt", Instant.now().toString());
    }

    public record PaymentRequest(String customerName, BigDecimal amount) {}
}
