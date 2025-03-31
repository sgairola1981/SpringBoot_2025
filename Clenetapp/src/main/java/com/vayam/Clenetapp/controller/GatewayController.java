package com.vayam.Clenetapp.controller;
import com.vayam.Clenetapp.dto.Order;
import com.vayam.Clenetapp.dto.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GatewayController {

    private final WebClient webClient;

    public GatewayController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
    @GetMapping("/user-details/{userId}")
    public Mono<Map<String, Object>> getUserDetails(@PathVariable String userId) {
        String userServiceUrl = "http://localhost:8081/users/" + userId;
        String orderServiceUrl = "http://localhost:8082/orders/user/" + userId;

        Mono<User> userMono = webClient.get().uri(userServiceUrl)
                .retrieve().bodyToMono(User.class);

        Mono<List<Order>> ordersMono = webClient.get().uri(orderServiceUrl)
                .retrieve().bodyToMono(new ParameterizedTypeReference<>() {});

        return Mono.zip(userMono, ordersMono)
                .map(tuple -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("user", tuple.getT1());
                    response.put("orders", tuple.getT2());
                    return response;
                });
    }
}
