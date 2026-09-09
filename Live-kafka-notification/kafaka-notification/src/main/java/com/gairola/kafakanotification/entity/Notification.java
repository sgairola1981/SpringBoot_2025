package com.gairola.kafakanotification.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq")
    @SequenceGenerator(
            name = "notification_seq",
            sequenceName = "NOTIFICATION_SEQ",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;

    @Column(name = "order_id", nullable = false, length = 100)
    private String orderId;

    @Column(name = "type", nullable = false, length = 80)
    private String type;

    @Column(name = "message", nullable = false, length = 1000)
    private String message;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "read", nullable = false)
    private boolean read = false;

    public Notification() {
    }

    public Notification(
            String userId,
            String orderId,
            String type,
            String message,
            Instant createdAt
    ) {
        this.userId = userId;
        this.orderId = orderId;
        this.type = type;
        this.message = message;
        this.createdAt = createdAt;
        this.read = false;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
    public void markRead() {
        this.read = true;
    }
}