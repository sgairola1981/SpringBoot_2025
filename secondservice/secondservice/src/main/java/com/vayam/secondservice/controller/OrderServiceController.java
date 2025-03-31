package com.vayam.secondservice.controller;
import com.vayam.secondservice.dto.Order;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderServiceController {

    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable String userId) {
        return List.of(
                new Order("O-1001", "Laptop", 1),
                new Order("O-1002", "Phone", 2)
        );
    }
}
