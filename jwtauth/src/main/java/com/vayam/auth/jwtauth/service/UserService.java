package com.vayam.auth.jwtauth.service;

import java.util.List;
import java.util.Optional;

import com.vayam.auth.jwtauth.exception.DuplicateDataExeception;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vayam.auth.jwtauth.dto.UserRegistrationDto;
import com.vayam.auth.jwtauth.model.BlacklistedToken;
import com.vayam.auth.jwtauth.model.User;
import com.vayam.auth.jwtauth.repository.BlacklistedTokenRepository;
import com.vayam.auth.jwtauth.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public UserRegistrationDto registerUser(UserRegistrationDto  userDto) {
        System.out.println("UserRegistrationDto !");
        Optional<User> existingUser ;
        User user;
       existingUser = userRepository.findById(userDto.getId());

            if (existingUser.isPresent()) {
                System.out.println("User ID  avaliable !");
                user = existingUser.get();
                user.setUsername(userDto.getUsername());
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
                user.setEmail(userDto.getEmail());
                user.setRoles("USERS");
                userRepository.save(user);
            }
         else {
           try {
               // Check if userid or email already exists
               if (userRepository.findAll().stream().anyMatch(user1 -> user1.getId().equals(userDto.getId()))) {
                   System.out.println("User Id allread Avaliable !");
                   throw new DuplicateDataExeception("UserId is already taken");
               }

               if (userRepository.findAll().stream().anyMatch(user1 -> user1.getEmail().equals(userDto.getEmail()))) {
                   System.out.println("User Email avaliable !");
                   throw new DuplicateDataExeception("Email is already registered");
               }
               System.out.println("Insert User ID ...!");
               user = new User();
               user.setId(userDto.getId());
               user.setUsername(userDto.getUsername());
               user.setPassword(passwordEncoder.encode(userDto.getPassword()));
               user.setEmail(userDto.getEmail());
               user.setRoles("USERS");
               userRepository.save(user);
               System.out.println("Insert User ID ... Save !");
           }
               catch (Exception e) {
                   System.out.println("Error...!" +e.getMessage());
                   throw new DuplicateDataExeception("Error on Save data "+ e.getMessage() +"!");
               }
        }

        return userDto;
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

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

    }


    public void deleteById(String id) {
        userRepository.deleteById(id);
    }
    public Page<User> getUsers(int page, int size, String sortField, String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));


        return userRepository.findAll(pageable);
    }
}
