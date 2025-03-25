package com.vayam.auth.jwtauth.controller;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.vayam.auth.jwtauth.dto.PageDTO;
import com.vayam.auth.jwtauth.exception.DuplicateDataExeception;
import org.springframework.data.domain.Page;
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
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto userData) {
        System.out.println("---registerUser!!");
        try {
            UserRegistrationDto savedUser = UserService.registerUser(userData); // Corrected method call
            return ResponseEntity.ok(savedUser);
        } catch (DuplicateDataExeception e) {
            // Handle user already exists error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            // Handle other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("error", "An unexpected error occurred."));
        }
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

  
    @GetMapping("/findList")
    public ResponseEntity<PageDTO<User>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection
          ) {
System.out.println("findList-->");
        Page<User> usersPage = UserService.getUsers(page, size, sortField, sortDirection);
        System.out.println("findList-->22222222222222222222221");

        PageDTO<User> pageDTO = new PageDTO<>();
        pageDTO.setContent(usersPage.getContent());
        pageDTO.setTotalPages(usersPage.getTotalPages());
        pageDTO.setTotalElements(usersPage.getTotalElements());
        pageDTO.setSize(usersPage.getSize());
        pageDTO.setNumber(usersPage.getNumber());
        return ResponseEntity.ok(pageDTO);
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
