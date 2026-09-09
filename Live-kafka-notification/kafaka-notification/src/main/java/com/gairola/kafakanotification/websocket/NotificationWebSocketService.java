package com.gairola.kafakanotification.websocket;

import com.gairola.kafakanotification.entity.Notification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationWebSocketService(
            SimpMessagingTemplate messagingTemplate) {

        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotification(Notification notification) {

        System.out.println();
        System.out.println("========================================");
        System.out.println("SENDING WEBSOCKET NOTIFICATION");
        System.out.println("User ID      : " + notification.getUserId());
        System.out.println("Notification : " + notification.getMessage());
        System.out.println("Destination  : /user/queue/notifications");
        System.out.println("========================================");

        messagingTemplate.convertAndSendToUser(
                notification.getUserId(),
                "/queue/notifications",
                notification
        );
    }
}