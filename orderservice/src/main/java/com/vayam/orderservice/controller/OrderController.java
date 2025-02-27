package com.vayam.orderservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @GetMapping
    public List<String> getOrders() {
        return List.of("Order1", "Order2", "Order3");
    }
}
