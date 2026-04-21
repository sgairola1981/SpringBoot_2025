package com.gairola.llm.config;

import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean("taskExecutor")
     public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.initialize();
        return executor;
    }
}