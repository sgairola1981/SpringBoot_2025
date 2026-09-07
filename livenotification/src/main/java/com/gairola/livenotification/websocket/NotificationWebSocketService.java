package com.gairola.livenotification.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationWebSocketService(
            SimpMessagingTemplate messagingTemplate) {

        this.messagingTemplate =
                messagingTemplate;
    }


    public void sendNotification(
            String userId,
            String message,
            String type) {

        System.out.println();
        System.out.println(
                "========================================"
        );

        System.out.println(
                "Sending WebSocket notification"
        );

        System.out.println(
                "User ID = " + userId
        );

        System.out.println(
                "Destination = /queue/notifications"
        );

        System.out.println(
                "Type = " + type
        );

        System.out.println(
                "Message = " + message
        );


        NotificationMessage notification =
                new NotificationMessage(
                        message,
                        type
                );


        messagingTemplate.convertAndSendToUser(
                userId,
                "/queue/notifications",
                notification
        );


        System.out.println(
                "WebSocket notification sent"
        );

        System.out.println(
                "========================================"
        );
    }


    public record NotificationMessage(
            String message,
            String type
    ) {
    }
}