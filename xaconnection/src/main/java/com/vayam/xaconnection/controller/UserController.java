package com.vayam.xaconnection.controller;
import com.vayam.xaconnection.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String showForm() {
        return "index";
    }

    @PostMapping("/saveUser")
    public String saveUser(@RequestParam String name, Model model) {
        userService.saveUser(name);
        model.addAttribute("message", "User saved successfully!");
        return "index";
    }
}
