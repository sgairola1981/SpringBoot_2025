package com.gairola.aidemo.controller;

import com.gairola.aidemo.entity.CallMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CallController {

    private final SimpMessagingTemplate template;

    @MessageMapping("/call")
    public void handleCallSignal(CallMessage message) {

        template.convertAndSendToUser(
                message.getReceiver(),
                "/queue/call",
                message
        );
    }
}