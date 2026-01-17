package com.gairola.GairolaSearchEngine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GairolaSearchEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(GairolaSearchEngineApplication.class, args);
        System.out.println("""
            🚀 AI Search Engine Ready!
            🌐 Visit: http://localhost:8888
            
            """);
	}

}
