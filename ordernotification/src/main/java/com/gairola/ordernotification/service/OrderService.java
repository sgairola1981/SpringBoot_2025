package com.gairola.ordernotification.service;


import com.gairola.ordernotification.grpc.NotificationRequest;
import com.gairola.ordernotification.grpc.NotificationResponse;
import com.gairola.ordernotification.grpc.NotificationServiceGrpc;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final NotificationServiceGrpc
            .NotificationServiceBlockingStub
            notificationClient;

    public OrderService(
            NotificationServiceGrpc
                    .NotificationServiceBlockingStub
                    notificationClient) {

        this.notificationClient =
                notificationClient;
    }

    public String createOrder(
            String userId) {

        String orderId =
                "ORD-" + System.currentTimeMillis();

        System.out.println(
                "Order created: " + orderId
        );

        // Normally save order into database here

        NotificationRequest request =
                NotificationRequest
                        .newBuilder()
                        .setUserId(userId)
                        .setMessage(
                                "Your order "
                                        + orderId
                                        + " has been confirmed"
                        )
                        .setType(
                                "ORDER_CONFIRMED"
                        )
                        .build();

        NotificationResponse response =
                notificationClient
                        .sendNotification(request);

        System.out.println(
                "gRPC response: "
                        + response.getMessage()
        );

        return orderId;
    }
}