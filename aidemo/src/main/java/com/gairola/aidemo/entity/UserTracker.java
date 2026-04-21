package com.gairola.aidemo.entity;


import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserTracker {

    private final SimpUserRegistry registry;

    public UserTracker(SimpUserRegistry registry) {
        this.registry = registry;
    }

    public Set<String> getOnlineUsers() {
        return registry.getUsers()
                .stream()
                .map(user -> user.getName())
                .collect(Collectors.toSet());
    }
}