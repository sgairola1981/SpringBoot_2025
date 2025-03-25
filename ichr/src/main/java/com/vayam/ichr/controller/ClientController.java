package com.vayam.ichr.controller;
import java.util.List;
import com.vayam.ichr.client.ClientService;
import com.vayam.ichr.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import com.vayam.ichr.client.AuthServiceClient;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/ICHR")
public class ClientController {
	
	 private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private WebClient webClient;
    @Value("${auth.service.url}")
     private String ServiceUrl;
    private final AuthServiceClient authServiceClient;
    @Autowired
    private ClientService userClient;
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

            String jwttoken=userClient.Login(user);
                model.addAttribute("message", "Login successful! Token: " + jwttoken);
                model.addAttribute("username", user.getUsername());
                // Add JWT to the response as an HTTP-only cookie
            System.out.println("Login Data---->"+ServiceUrl+"/api/auth/register");
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
            userClient.Logout(jwtToken);
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
    public String registerUser(@ModelAttribute UserData userData, Model model) {
        try {
            // Save the user
            UserData savedUser=userClient.registerUser(userData);

            if (savedUser != null) {
                model.addAttribute("message", "Data saved successfully!");
            } else {
                model.addAttribute("message", "Failed to save data.");
            }

        } catch (Exception e) {
            model.addAttribute("message", "Error saving data: " + e.getMessage());
            e.printStackTrace();
        }

        List<UserData> users=userClient.GetUserList();
        model.addAttribute("users", users);
        return "listusers";
    }

    @GetMapping("/listuser")
    public String listUsers(Model model) {
        // Fetch users from the backend API
        List<UserData> users=userClient.GetUserList();
      model.addAttribute("users", users);
      return "listusers"; // Thymeleaf template
    }

    @GetMapping("/listData")
          public String  getUsers(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 @RequestParam(defaultValue = "id") String sortField,
                                 @RequestParam(defaultValue = "asc") String sortDirection,
                                 Model model) {


        PageDTO<UserData> userPage = userClient.getUsers(page, size, sortField, sortDirection);
        System.out.println("findList-->11111111111111111111");
        model.addAttribute("userPage", userPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");


        return "userDetails"; // Thymeleaf template
          }



     @GetMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id,Model model) {
        // Delete user by ID

         userClient.DeleteUser(id);
         model.addAttribute("message", "record Delete Sucessfully !");
         List<UserData> users=userClient.GetUserList();
       model.addAttribute("users", users);
        return "listusers"; // Thymeleaf template
        
    }
    
    @GetMapping("/users/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model) {
         UserData user= userClient.EditUserDetails(id);
        model.addAttribute("user", user);
        return "newuser";
    }

}