package com.gairola.ordernotification.controller;


import com.gairola.ordernotification.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(
            OrderService orderService) {

        this.orderService =
                orderService;
    }

    @PostMapping
    public String createOrder(
            @RequestParam String userId) {

        return orderService.createOrder(
                userId
        );
    }
}