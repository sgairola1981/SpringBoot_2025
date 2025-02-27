package com.vayam.auth.jwtauth.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vayam.auth.jwtauth.dto.UserRegistrationDto;
import com.vayam.auth.jwtauth.model.BlacklistedToken;
import com.vayam.auth.jwtauth.model.User;
import com.vayam.auth.jwtauth.repository.BlacklistedTokenRepository;
import com.vayam.auth.jwtauth.repository.UserRepository;

import io.jsonwebtoken.Jwts;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
     private final BlacklistedTokenRepository tokenRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,BlacklistedTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

    public void registerUser(UserRegistrationDto  userDto) {
        // Check if username or email already exists
           if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        if (userRepository.findAll().stream().anyMatch(user -> user.getEmail().equals(userDto.getEmail()))) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // Create and save the new user
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // Hash the password
        user.setEmail(userDto.getEmail());
        user.setRoles("USERS");

        userRepository.save(user);
    }
     public boolean logout(String token) {

        try {
            BlacklistedToken blacklistedToken = new BlacklistedToken();
            blacklistedToken.setToken(token);
            tokenRepository.save(blacklistedToken);
            return true;
        } catch (Exception e) {
            return false;
        }
       
    }
    public boolean isTokenValid(String token) {
        return !tokenRepository.existsByToken(token);
    }
 
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
