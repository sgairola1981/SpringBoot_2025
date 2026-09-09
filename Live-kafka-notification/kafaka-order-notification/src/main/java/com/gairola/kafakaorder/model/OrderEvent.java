package com.gairola.kafakaorder.model;

import java.time.Instant;

public record OrderEvent(

        String orderId,

        String userId,

        String eventType,

        String message,

        Instant createdAt

) {
}
