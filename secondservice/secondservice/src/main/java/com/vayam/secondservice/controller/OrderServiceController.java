package com.vayam.secondservice.controller;
import com.vayam.secondservice.dto.Order;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order Controller", description = "Order Controller")
public class OrderServiceController {

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user by ID")
    public List<Order> getOrdersByUser(@PathVariable String userId) {
        return List.of(
                new Order("O-1001", "Laptop", 1),
                new Order("O-1002", "Phone", 2)
        );
    }
    @GetMapping("/users")
    @Operation(summary = "Get All Order")
    public List<Order> getAllOrders() {
        return List.of(
                new Order("O-1001", "Laptop", 1),
                new Order("O-1002", "Phone", 2)
        );
    }  

}
