package com.gairola.kafakaorder.model;


import java.time.Instant;

public record Order(

        String orderId,

        String userId,

        String status,

        Instant createdAt

) {
}