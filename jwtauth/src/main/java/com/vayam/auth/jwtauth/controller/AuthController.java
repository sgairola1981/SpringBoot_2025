package com.vayam.auth.jwtauth.controller;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.vayam.auth.jwtauth.dto.AuthRequest;
import com.vayam.auth.jwtauth.dto.UserRegistrationDto;
import com.vayam.auth.jwtauth.exception.InvalidCredentialsException;
import com.vayam.auth.jwtauth.model.User;
import com.vayam.auth.jwtauth.service.UserService;
import com.vayam.auth.jwtauth.util.JwtUtils;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService UserService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService UserService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.UserService = UserService;
    }

    // @PostMapping("/register")
  /*  public String register(@Valid @RequestBody UserRegistrationDto userDto,Model model) {
          try {
            System.out.println(userDto.getEmail()+"------------------------");
            UserService.registerUser(userDto);
               model.addAttribute("message", "User registered successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
        }
        return "User registered successfully";
    }*/
     @PostMapping("/register")
    public ResponseEntity<UserRegistrationDto> registerUser(@RequestBody UserRegistrationDto userData) {
        UserService.registerUser(userData);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON) // Ensure JSON response
                .body(userData);
    }
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        try {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
          return jwtUtils.generateToken(user.getUsername());
         } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }
    @PostMapping("/login_c")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
      
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      
           String token = jwtUtils.generateToken(username);
           
            return ResponseEntity.ok(Map.of("token", token));

        
       // return ResponseEntity.status(401).body("Invalid credentials");
    }

    @PostMapping("/submit")
    public String submitForm(@ModelAttribute AuthRequest user) {
        // Send data to the backend microservice
      //  restTemplate.postForObject(BACKEND_URL, AuthRequest, String.class);
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
      String token = jwtUtils.generateToken(user.getUsername());  
      System.out.println(token);
      
      
      return "home";
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            boolean isValid= UserService.logout(token.substring(7));
            if (isValid) {
                // Optionally, add to a blacklist or invalidate session
                return ResponseEntity.ok("Logout successful"); // Token is valid and handled
            }
            return ResponseEntity.ok("Invalid Token"); // Token is invalid
        }
        return ResponseEntity.ok("Invalid ");
    }

  
    @GetMapping("/FIND_ALL_USER")
    public List<User> listUsers(Model model) {
       // model.addAttribute("users", UserService.findAll());
        return UserService.findAll();
    }
    @GetMapping("/FIND_ALL_USER/{id}/edit")
    public User  editUserForm(@PathVariable Long id, Model model) {
      //  model.addAttribute("user", UserService.findById(id));
        return UserService.findById(id);
    }
    @DeleteMapping("FIND_ALL_USER/{id}/delete")
    public void deleteUser(@PathVariable Long id) {
        
        UserService.deleteById(id);
      
    }  

    @GetMapping("/main")
    public String login() {
        return "login"; // Refers to the login.html Thymeleaf template
    }

    @GetMapping("/home")
    public String home() {
        return "home"; // Refers to the home.html Thymeleaf template
    }
}
