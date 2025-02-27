package com.vayam.ichr.controller;



import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vayam.ichr.client.AuthServiceClient;
import com.vayam.ichr.dto.AuthRequest;
import com.vayam.ichr.dto.Employee;
import com.vayam.ichr.dto.UserData;

import io.netty.handler.codec.http.HttpHeaders;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import reactor.core.publisher.Mono;


@Controller
@RequestMapping("/ICHR")
public class ClientController {
	
	 private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private WebClient webClient;
   /* 
     private final String AUTH_URL = "http://localhost:8888/api/auth";
    private final String AUTH_URL_LOGIN = "http://localhost:8888/api/auth/login";
    private final String AUTH_URL_LOGOUT = "http://localhost:8888/api/auth/logout";
    private final String AUTH_URL_REG = "http://localhost:8888/api/auth/register";
    private final String AUTH_URL_LIST = "http://localhost:8888/api/auth/register";
*/
    private final String AUTH_URL = "http://localhost:9001/api/auth";
    private final String AUTH_URL_LOGIN = "http://localhost:9001/api/auth/login";
    private final String AUTH_URL_LOGOUT = "http://localhost:9001/api/auth/logout";
    private final String AUTH_URL_REG = "http://localhost:9001/api/auth/register";
    private final String AUTH_URL_LIST = "http://localhost:9001/api/auth/register";
    //9001
    

    private final AuthServiceClient authServiceClient;

    public ClientController(AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }
      
    @GetMapping("/m")
    public String welcome() {
        return "index"; // Refers to the login.html Thymeleaf template
    }
    @GetMapping("/form")
    public String form(Model model) {
    	 logger.info("ichr-service - Received request on //ICHR/form");
        model.addAttribute("user", new UserData());
        return "newuser";
    }
       
    @PostMapping("/submit")
    public String submitForm(@ModelAttribute AuthRequest user, Model model,HttpServletResponse response) {
        // Use WebClient to send data to the backend microservice
        String url;
        try {  
        String jwttoken=  webClient.post()
                 .uri(AUTH_URL_LOGIN)
                 .bodyValue(user)
                 .retrieve()
                 .bodyToMono(String.class)
                 .block(); // Blocking for simplicity; in production, use reactive programming
                 System.out.println("00000000000000"+jwttoken);
                model.addAttribute("message", "Login successful! Token: " + jwttoken);
                model.addAttribute("username", user.getUsername());
                // Add JWT to the response as an HTTP-only cookie
                 System.out.println("Token ===="+jwttoken);
                 Cookie cookie = new Cookie("token",jwttoken);
                 cookie.setMaxAge(Integer.MAX_VALUE);
                 
                 response.addCookie(cookie);
                 
                url="index";
                } catch (Exception e) {
                    System.out.println("4444444444444444444444444444444444"+e);
                    model.addAttribute("message", "Invalid credentials");
                    url="login";
                }
        
        return url;
    }
    @PostMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse res,Model m,HttpSession session)
    {
             String url;
        try {  
            
String jwtToken="";
            Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    jwtToken= cookie.getValue();
                }
            }
        }
		 Cookie[] cookies2 = request.getCookies();
		 for(int i = 0; i < cookies2.length; i++) 
		 {
		 	if (cookies2[i].getName().equals("token")) 
		 	{
		     cookies2[i].setMaxAge(0);
		     res.addCookie(cookies2[i]);
		 	
		  }
        }
          System.out.println("sssssssssssssssssssssssssssssssssss" +jwtToken);
        webClient.post()
                 .uri(AUTH_URL_LOGOUT)
                 .header("Authorization", "Bearer " + jwtToken)
                 .retrieve()
                 .toBodilessEntity()
                 .block(); // Blocking for simplicity; in production, use reactive programming
                 System.out.println("wwwwwwwwwwwwwwwwwwwwwwwww");
               url="login";
                } catch (Exception e) {
                    System.out.println("ffffffffffffffffffffffffffffffff"+e);
                  url="login";
                }
        
        return url;
    }
    
   
    @GetMapping("/login")
    public String login() {
        return "login"; // Refers to the login.html Thymeleaf template
    }
    @GetMapping("/test")
    public String test(Model model) {
        model.addAttribute("user", new UserData());
        return "test"; // Refers to the home.html Thymeleaf template
    }

    @GetMapping("/home")
    public String home() {
        return "home"; // Refers to the home.html Thymeleaf template
    }
    @GetMapping("/regestration")
    public String registration(Model model) {
        System.out.println("registration");
        model.addAttribute("data", new UserData());
                   return "registration"; // Refers to the regestration.html Thymeleaf template
    }
     // Show form to add a new user
     @GetMapping("/newuser")
     public String showCreateForm(Model model) {
         model.addAttribute("user", new UserData());
         return "newuser";
     }


     

        @PostMapping("/saveuser")
        public String registerUser(UserData userData ,Model model) {
           webClient.post()
                .uri(AUTH_URL_REG)
                .bodyValue(userData)
                .retrieve()
                .onStatus(status -> status.isError(), response -> 
                    response.bodyToMono(String.class)
                            .flatMap(error -> Mono.error(new RuntimeException("Error: " + error)))
                )
                .bodyToMono(UserData.class);
                model.addAttribute("message", "Data Save Sucessfully !");
              //  model.addAttribute("userData", new UserData());

              List<UserData> users = webClient.get()
               .uri(AUTH_URL+"/FIND_ALL_USER")
               .retrieve()
               .bodyToFlux(UserData.class)
               .collectList()
               .block(); // Blocking for simplicity
       model.addAttribute("users", users);
                return "listusers";
    }

       @GetMapping("/listuser")
    public String listUsers(Model model) {
        // Fetch users from the backend API
        List<UserData> users = webClient.get()
                .uri(AUTH_URL+"/FIND_ALL_USER")
                .retrieve()
                .bodyToFlux(UserData.class)
                .collectList()
                .block(); // Blocking for simplicity
        model.addAttribute("users", users);
        return "listusers"; // Thymeleaf template
    }

     @GetMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id,Model model) {
        // Delete user by ID
        webClient.delete()
                .uri(AUTH_URL+"/FIND_ALL_USER/{id}/delete", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
                model.addAttribute("message", "record Delete Sucessfully !"); 
               /////////////////////////////////
               List<UserData> users = webClient.get()
               .uri(AUTH_URL+"/FIND_ALL_USER")
               .retrieve()
               .bodyToFlux(UserData.class)
               .collectList()
               .block(); // Blocking for simplicity
       model.addAttribute("users", users);
         /////////////////////////////////
       return "listusers"; // Thymeleaf template         
        
    }
    
    @GetMapping("/users/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model) {
        // Fetch user by ID
        UserData user = webClient.get()
                .uri(AUTH_URL+"/FIND_ALL_USER/{id}/edit", id)
                .retrieve()
                .bodyToMono(UserData.class)
                .block();
        model.addAttribute("user", user);
        return "newuser";
    }

     
}