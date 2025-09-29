package com.gairola.LoggingTracing.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class HelloController {

    private final AtomicInteger counter = new AtomicInteger();

    @GetMapping("/hello")
    public String hello() {
        counter.incrementAndGet();
        return "Hello! Count = " + counter.get();
    }


}