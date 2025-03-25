package com.vayam.ichr.client;


import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.vayam.ichr.dto.AuthRequest;

import reactor.core.publisher.Mono;

@Service
public class AuthServiceClient {

    private final WebClient webClient;

    public AuthServiceClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:9001/api/auth").build();
    }

    public Map<String,String> validateUser(String username, String password) {
        //AuthRequest authRequest = new AuthRequest(username, password);

       
       Map<String, String> response = webClient.post()
                    .uri("/login_c")
                    .bodyValue(Map.of("username", username, "password", password))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
       return response;
       

                    }
}
