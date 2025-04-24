package com.gairola.chat.model;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnlineUserTracker {

    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

    public void addUser(String username) {
        onlineUsers.add(username);
    }

    public void removeUser(String username) {
        onlineUsers.remove(username);
    }

    public Set<String> getOnlineUsers() {
        return onlineUsers;
    }
}
