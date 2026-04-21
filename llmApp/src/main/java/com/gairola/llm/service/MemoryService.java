package com.gairola.llm.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
    public String getLastN(String sessionId, int n) {

        List<String> chat = memory.getOrDefault(sessionId, new ArrayList<>());

        return chat.stream()
                .skip(Math.max(0, chat.size() - n * 2))
                .collect(Collectors.joining("\n"));
    }
    public void save(String sessionId, String question, String answer) {

        memory.computeIfAbsent(sessionId, k -> new ArrayList<>());

        List<String> chat = memory.get(sessionId);

        chat.add("User: " + question);
        chat.add("AI: " + answer);

        // keep last 10 entries only
        if (chat.size() > 10) {
            chat.subList(0, chat.size() - 10).clear();
        }
    }


}