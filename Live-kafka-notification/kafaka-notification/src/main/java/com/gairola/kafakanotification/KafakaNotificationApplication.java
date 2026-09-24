package com.gairola.kafakanotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
public class KafakaNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafakaNotificationApplication.class, args);
	}

}
