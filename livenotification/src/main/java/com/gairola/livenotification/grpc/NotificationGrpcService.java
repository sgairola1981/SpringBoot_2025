    package com.gairola.livenotification.grpc;

import com.gairola.livenotification.websocket.NotificationWebSocketService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class NotificationGrpcService
        extends NotificationServiceGrpc.NotificationServiceImplBase {

    private final NotificationWebSocketService webSocketService;

    public NotificationGrpcService(
            NotificationWebSocketService webSocketService) {

        this.webSocketService = webSocketService;
    }

    @Override
    public void sendNotification(
            NotificationRequest request,
            StreamObserver<NotificationResponse> responseObserver) {

        try {

            // Validate request
            if (request == null) {
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("Notification request cannot be null")
                                .asRuntimeException()
                );
                return;
            }

            String userId = request.getUserId();
            String message = request.getMessage();
            String type = request.getType();

            if (userId == null || userId.isBlank()) {
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("userId is required")
                                .asRuntimeException()
                );
                return;
            }

            if (message == null || message.isBlank()) {
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("message is required")
                                .asRuntimeException()
                );
                return;
            }

            // Log request
            System.out.println(
                    "Notification received: "
                            + "user=" + userId
                            + ", type=" + type
                            + ", message=" + message
            );

            // Send notification to browser
            webSocketService.sendNotification(
                    userId,
                    message,
                    type
            );

            // Build response
            NotificationResponse response =
                    NotificationResponse.newBuilder()
                            .setSuccess(true)
                            .setMessage("Notification delivered")
                            .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {

            System.err.println(
                    "Failed to send notification: "
                            + e.getMessage()
            );

            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription(
                                    "Failed to deliver notification"
                            )
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }
}
