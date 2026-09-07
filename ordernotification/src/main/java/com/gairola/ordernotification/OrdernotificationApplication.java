package com.gairola.ordernotification;

import com.gairola.ordernotification.grpc.NotificationServiceGrpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.grpc.client.ImportGrpcClients;

@SpringBootApplication
@ImportGrpcClients(
		target = "notification",
		types = NotificationServiceGrpc
				.NotificationServiceBlockingStub.class
)
public class OrdernotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdernotificationApplication.class, args);
	}

}
