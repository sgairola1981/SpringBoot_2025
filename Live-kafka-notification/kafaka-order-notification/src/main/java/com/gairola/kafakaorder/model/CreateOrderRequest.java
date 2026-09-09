package com.gairola.kafakaorder.model;


import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequest(

        @NotBlank(message = "userId is required")
        String userId

) {
}
