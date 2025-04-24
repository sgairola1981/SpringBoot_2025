package com.gairola.chat.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Component
public class WebSocketEventListener {

    @Autowired
    private OnlineUserTracker tracker;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        Principal user = (Principal) event.getUser();
        if (user != null) {
            tracker.addUser(user.getName());
            messagingTemplate.convertAndSend("/topic/online", tracker.getOnlineUsers());
        }
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        Principal user = (Principal) event.getUser();
        if (user != null) {
            tracker.removeUser(user.getName());
            messagingTemplate.convertAndSend("/topic/online", tracker.getOnlineUsers());
        }
    }
}
