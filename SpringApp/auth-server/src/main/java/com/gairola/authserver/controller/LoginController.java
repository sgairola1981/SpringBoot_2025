package com.gairola.authserver.controller;

import com.gairola.authserver.dto.UserRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {


    @GetMapping("/login")
    public String login() {

        return "login";
    }

    @GetMapping("/")
    public String login1() {

        return "login";
    }
    @GetMapping("/create")
    public String showUserForm(Model model) {
        model.addAttribute("userRequest", new UserRequest());
        return "user-create";
    }

    }