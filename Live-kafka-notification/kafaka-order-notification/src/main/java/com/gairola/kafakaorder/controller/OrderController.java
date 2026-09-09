package com.gairola.kafakaorder.controller;
import com.gairola.kafakaorder.model.CreateOrderRequest;
import com.gairola.kafakaorder.model.Order;
import com.gairola.kafakaorder.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(
            OrderService orderService) {

        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(

            @Valid
            @RequestBody
            CreateOrderRequest request) {

        return orderService.createOrder(request);
    }

    @GetMapping
    public List<Order> getOrders() {

        return orderService.findAll();
    }
}
