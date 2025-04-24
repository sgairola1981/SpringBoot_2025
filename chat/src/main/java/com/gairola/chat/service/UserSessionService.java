package com.gairola.chat.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserSessionService {
    private final Set<String> activeUsers = ConcurrentHashMap.newKeySet();

    public void addUser(String username) {
        activeUsers.add(username);
    }

    public void removeUser(String username) {
        activeUsers.remove(username);
    }

    public Set<String> getOnlineUsers(String currentUser) {
        return activeUsers.stream()
                .filter(user -> !user.equals(currentUser))
                .collect(java.util.stream.Collectors.toSet());
    }
}
