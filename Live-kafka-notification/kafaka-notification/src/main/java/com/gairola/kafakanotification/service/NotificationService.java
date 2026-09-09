package com.gairola.kafakanotification.service;



import com.gairola.kafakanotification.entity.Notification;
import com.gairola.kafakanotification.repository.NotificationRepository;
import com.gairola.kafakanotification.websocket.NotificationWebSocketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    private final NotificationWebSocketService
            webSocketService;

    public NotificationService(

            NotificationRepository repository,

            NotificationWebSocketService
                    webSocketService) {

        this.repository = repository;

        this.webSocketService =
                webSocketService;
    }

    @Transactional
    public Notification createAndSend(

            String userId,

            String orderId,

            String type,

            String message,

            Instant createdAt) {

        Notification notification =

                new Notification(

                        userId,

                        orderId,

                        type,

                        message,

                        createdAt
                );

        notification =
                repository.save(notification);

        webSocketService.sendNotification(
                notification
        );

        return notification;
    }

    public List<Notification>
    findByUser(String userId) {

        return repository
                .findByUserIdOrderByCreatedAtDesc(
                        userId
                );
    }

    public long unreadCount(String userId) {

        return repository
                .countByUserIdAndReadFalse(
                        userId
                );
    }

    @Transactional
    public void markRead(Long id) {

        repository
                .findById(id)
                .ifPresent(notification -> {

                    notification.markRead();

                    repository.save(notification);
                });
    }

    @Transactional
    public void markAllRead(String userId) {

        repository
                .findByUserIdAndReadFalse(userId)
                .forEach(notification -> {

                    notification.markRead();

                    repository.save(notification);
                });
    }

    @Transactional
    public void clearAll(String userId) {

        repository.deleteAll(

                repository
                        .findByUserIdOrderByCreatedAtDesc(
                                userId
                        )
        );
    }
}