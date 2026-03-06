package com.gairola.llm.config;

import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.*;

import java.util.concurrent.*;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean("taskExecutor")
    public Executor taskExecutor() {

        return new ThreadPoolExecutor(
                5,
                10,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
    }
}