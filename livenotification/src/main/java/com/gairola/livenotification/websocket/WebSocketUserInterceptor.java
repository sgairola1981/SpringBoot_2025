package com.gairola.livenotification.websocket;

import java.security.Principal;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class WebSocketUserInterceptor
        implements ChannelInterceptor {

    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel) {

        /*
         * IMPORTANT:
         *
         * Do NOT use:
         *
         * StompHeaderAccessor.wrap(message)
         *
         * here.
         *
         * We need the existing accessor created by
         * Spring's STOMP infrastructure so that Spring
         * can retain the Principal for the session.
         */
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


        /*
         * =====================================================
         * CONNECT
         * =====================================================
         */

        if (StompCommand.CONNECT.equals(command)) {

            String userId =
                    accessor.getFirstNativeHeader("userId");

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


            if (userId != null &&
                    !userId.isBlank()) {

                /*
                 * Create Principal.
                 */
                Principal principal =
                        () -> userId;


                /*
                 * IMPORTANT:
                 *
                 * Set Principal directly on the
                 * existing STOMP accessor.
                 *
                 * Spring Framework will retain this
                 * Principal for subsequent messages.
                 */
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


        /*
         * =====================================================
         * SUBSCRIBE
         * =====================================================
         */

        if (StompCommand.SUBSCRIBE.equals(command)) {

            System.out.println();
            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "WebSocket SUBSCRIBE"
            );

            System.out.println(
                    "Destination = "
                            + accessor.getDestination()
            );


            Principal principal =
                    accessor.getUser();


            if (principal != null) {

                System.out.println(
                        "Principal = "
                                + principal.getName()
                );

            } else {

                System.out.println(
                        "Principal = NULL"
                );
            }


            System.out.println(
                    "========================================"
            );
        }


        /*
         * =====================================================
         * DISCONNECT
         * =====================================================
         */

        if (StompCommand.DISCONNECT.equals(command)) {

            System.out.println();
            System.out.println(
                    "WebSocket DISCONNECT"
            );


            Principal principal =
                    accessor.getUser();


            if (principal != null) {

                System.out.println(
                        "Principal = "
                                + principal.getName()
                );
            }
        }


        /*
         * IMPORTANT:
         *
         * Return the ORIGINAL message.
         *
         * Do NOT rebuild it using MessageBuilder.
         */
        return message;
    }
}