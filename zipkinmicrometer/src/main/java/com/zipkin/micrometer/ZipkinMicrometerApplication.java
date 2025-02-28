package com.zipkin.micrometer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ZipkinMicrometerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZipkinMicrometerApplication.class, args);
	}

}
