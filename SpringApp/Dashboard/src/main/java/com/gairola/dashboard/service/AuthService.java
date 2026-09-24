package com.gairola.dashboard.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthService {

    private final RestTemplate restTemplate;

    @Value("${auth.server.url}")
    private String authServerUrl;

    public AuthService() {
        this.restTemplate = new RestTemplate();
    }

    public Map<String, Object> login(String username, String password) {

        String url = authServerUrl + "/api/auth/login";

        Map<String, String> request = Map.of(
                "username", username,
                "password", password
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity =
                new HttpEntity<>(request, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(
                        url,
                        entity,
                        Map.class
                );

        if (response.getStatusCode() == HttpStatus.OK
                && response.getBody() != null) {

            return response.getBody();
        }

        throw new RuntimeException("Authentication failed");
    }
}