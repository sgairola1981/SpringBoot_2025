package com.gairola.authserver.controller;

import com.gairola.authserver.entity.AppUser;
import com.gairola.authserver.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserFormController {

    private final UserService userService;

    public UserFormController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public String listUsers(Model model) {

        model.addAttribute(
                "users",
                userService.findAll()
        );

        return "user-list";
    }

    @GetMapping("/create")
    public String createPage(Model model) {

        model.addAttribute(
                "user",
                new AppUser()
        );

        model.addAttribute(
                "mode",
                "create"
        );

        return "user-form";
    }

    @PostMapping("/create")
    public String createUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String role,
            Model model) {

        try {

            userService.createUser(
                    username,
                    password,
                    role
            );

            return "redirect:/user/list";

        } catch (Exception e) {

            model.addAttribute(
                    "error",
                    e.getMessage()
            );

            AppUser user = new AppUser();
            user.setUsername(username);
            user.setRole(role);

            model.addAttribute("user", user);
            model.addAttribute("mode", "create");

            return "user-form";
        }
    }

    // ============================
    // EDIT USER
    // ============================

    @GetMapping("/edit/{id}")
    public String editUser(
            @PathVariable Long id,
            Model model) {

        AppUser user = userService.findById(id);

        model.addAttribute(
                "user",
                user
        );

        model.addAttribute(
                "mode",
                "edit"
        );

        return "user-form";
    }

    // ============================
    // UPDATE USER
    // ============================

    @PostMapping("/update")
    public String updateUser(
            @RequestParam Long id,
            @RequestParam String username,
            @RequestParam(required = false) String password,
            @RequestParam String role,
            @RequestParam Boolean enabled,
            Model model) {

        try {

            userService.updateUser(
                    id,
                    username,
                    password,
                    role,
                    enabled
            );

            return "redirect:/user/list";

        } catch (Exception e) {

            model.addAttribute(
                    "error",
                    e.getMessage()
            );

            AppUser user = userService.findById(id);

            user.setUsername(username);
            user.setRole(role);
            user.setEnabled(enabled);

            model.addAttribute(
                    "user",
                    user
            );

            model.addAttribute(
                    "mode",
                    "edit"
            );

            return "user-form";
        }
    }

    // ============================
    // DELETE
    // ============================

    @GetMapping("/delete/{id}")
    public String deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return "redirect:/user/list";
    }
}