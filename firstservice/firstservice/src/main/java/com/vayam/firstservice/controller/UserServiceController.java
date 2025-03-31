package com.vayam.firstservice.controller;

import com.vayam.firstservice.dto.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserServiceController {

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        return new User(id, "John Doe", "john.doe@example.com");
    }
}

