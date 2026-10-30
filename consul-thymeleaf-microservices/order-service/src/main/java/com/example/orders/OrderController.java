package com.example.orders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final List<OrderDto> orders = new CopyOnWriteArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1000);
    private final WebClient.Builder webClientBuilder;

    public OrderController(WebClient.Builder webClientBuilder) { this.webClientBuilder = webClientBuilder; }

    @GetMapping
    public List<OrderDto> list() { return List.copyOf(orders); }

    @PostMapping
    public OrderDto create(@RequestBody CreateOrderRequest request) {
        if (request.customerName() == null || request.customerName().isBlank()
                || request.amount() == null || request.amount().signum() <= 0) {
            throw new IllegalArgumentException("customerName and a positive amount are required");
        }
        Map<?, ?> payment = webClientBuilder.build().post()
            .uri("http://payment-service/api/payments")
            .bodyValue(Map.of("amount", request.amount(), "customerName", request.customerName().trim()))
            .retrieve().bodyToMono(Map.class).block();
        String paymentStatus = payment == null ? "UNKNOWN" : String.valueOf(payment.get("status"));
        OrderDto order = new OrderDto(sequence.incrementAndGet(), request.customerName().trim(),
            request.amount(), "CREATED", paymentStatus, Instant.now().toString());
        orders.add(0, order);
        return order;
    }

    public record CreateOrderRequest(String customerName, BigDecimal amount) {}
    public record OrderDto(long id, String customerName, BigDecimal amount, String status,
                           String paymentStatus, String createdAt) {}
}
