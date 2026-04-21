package com.gairola.aidemo.controller;

import com.gairola.aidemo.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor

public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/private-message")
    public void sendPrivateMessage(ChatMessage message, Principal principal) {

        String sender = principal.getName(); // 🔥 real logged user

        message.setSender(sender);

        messagingTemplate.convertAndSendToUser(
                message.getReceiver(),
                "/queue/messages",
                message
        );
    }
}