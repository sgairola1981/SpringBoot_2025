package com.gairola.kafakanotification.websocket;

import java.security.Principal;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import org.springframework.messaging.support.MessageHeaderAccessor;

import org.springframework.messaging.support.ChannelInterceptor;

import org.springframework.stereotype.Component;

@Component
public class WebSocketUserInterceptor
        implements ChannelInterceptor {

    @Override
    public Message<?> preSend(

            Message<?> message,

            MessageChannel channel) {

        StompHeaderAccessor accessor =

                MessageHeaderAccessor.getAccessor(

                        message,

                        StompHeaderAccessor.class
                );

        if (accessor == null) {
            return message;
        }

        StompCommand command =
                accessor.getCommand();

        if (StompCommand.CONNECT.equals(command)) {

            String userId =

                    accessor.getFirstNativeHeader(
                            "userId"
                    );

            System.out.println();
            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "WebSocket CONNECT"
            );

            System.out.println(
                    "userId = " + userId
            );

            if (userId != null
                    && !userId.isBlank()) {

                Principal principal =
                        () -> userId;

                accessor.setUser(principal);

                System.out.println(
                        "Principal set = "
                                + accessor
                                .getUser()
                                .getName()
                );
            }

            System.out.println(
                    "========================================"
            );
        }

        if (StompCommand.SUBSCRIBE.equals(command)) {

            Principal principal =
                    accessor.getUser();

            System.out.println();

            System.out.println(
                    "WebSocket SUBSCRIBE"
            );

            System.out.println(
                    "Destination = "
                            + accessor.getDestination()
            );

            System.out.println(
                    "Principal = "
                            + (
                            principal == null
                                    ? "NULL"
                                    : principal.getName()
                    )
            );
        }

        return message;
    }
}