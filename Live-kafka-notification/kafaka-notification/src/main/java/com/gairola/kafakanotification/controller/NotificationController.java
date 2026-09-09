package com.gairola.kafakanotification.controller;


import com.gairola.kafakanotification.entity.Notification;
import com.gairola.kafakanotification.service.NotificationService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(
            NotificationService service) {

        this.service = service;
    }

    @GetMapping("/user/{userId}")
    public List<Notification>
    getUserNotifications(

            @PathVariable
            String userId) {

        return service.findByUser(userId);
    }

    @GetMapping("/user/{userId}/unread-count")
    public Map<String, Long>
    unreadCount(

            @PathVariable
            String userId) {

        return Map.of(
                "count",
                service.unreadCount(userId)
        );
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void>
    markRead(

            @PathVariable
            Long id) {

        service.markRead(id);

        return ResponseEntity.noContent()
                .build();
    }

    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<Void>
    markAllRead(

            @PathVariable
            String userId) {

        service.markAllRead(userId);

        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void>
    clearAll(

            @PathVariable
            String userId) {

        service.clearAll(userId);

        return ResponseEntity.noContent()
                .build();
    }
}