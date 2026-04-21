package com.gairola.aidemo.controller;



import com.gairola.aidemo.entity.UserTracker;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {

    private final UserTracker tracker;

    @GetMapping("/online")
    public Set<String> onlineUsers() {
        return tracker.getOnlineUsers();
    }
}