package com.gairola.localprompt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class LocalpromptApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocalpromptApplication.class, args);
	}

}
