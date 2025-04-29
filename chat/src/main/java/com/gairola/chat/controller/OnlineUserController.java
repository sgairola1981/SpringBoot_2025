package com.gairola.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class OnlineUserController {
    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/online")
    public void handleOnlineUser(Principal principal) {
        onlineUsers.add(principal.getName());

        messagingTemplate.convertAndSend("/topic/online", onlineUsers);
    }
}

