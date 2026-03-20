package com.gairola.llm.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MemoryService {

    private final Map<String, List<String>> memory = new ConcurrentHashMap<>();

    public void append(String sessionId, String role, String message) {

        memory.computeIfAbsent(sessionId, k -> new ArrayList<>())
                .add(role + ": " + message);
    }

    public String getConversation(String sessionId) {

        List<String> history = memory.getOrDefault(sessionId, new ArrayList<>());

        return String.join("\n", history);
    }
}