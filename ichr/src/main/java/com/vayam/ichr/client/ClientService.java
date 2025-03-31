package com.vayam.ichr.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vayam.ichr.dto.AuthRequest;
import com.vayam.ichr.dto.PageDTO;
import com.vayam.ichr.dto.UserData;
import com.vayam.ichr.security.JwtTokenStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ClientService {

    private final WebClient webClient;

    @Value("${auth.service.url}")
    private String userServiceUrl;

    public ClientService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(userServiceUrl).build();
    }

    public PageDTO<UserData> getUsers(int page, int size, String sortField, String sortDirection) {

        String Link=userServiceUrl+"/api/auth/findList";
        System.out.println("Link---->"+Link);
        PageDTO<UserData> userPage =  webClient.get()
                        .uri(Link,uriBuilder -> uriBuilder
                                .queryParam("page", page)
                                .queryParam("size", size).
                                queryParam("sortField", sortField)
                                .queryParam("sortDirection", sortDirection)
                                .build())
                .headers(headers -> headers.setBearerAuth(JwtTokenStorage.getToken())) // Send JWT token
                .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<PageDTO<UserData>>() {})
                        .block();

return userPage;
    }
   public List<UserData> GetUserList()
    {
        List<UserData> users = webClient.get()
                .uri(userServiceUrl+"/api/auth/FIND_ALL_USER")
                .headers(headers -> headers.setBearerAuth(JwtTokenStorage.getToken())) // Send JWT token
                .retrieve()
                .bodyToFlux(UserData.class)
                .collectList()
                .block(); // Blocking for simplicity
        return users;
    }


    public void DeleteUser(String userid) {

        webClient.delete()
                .uri(userServiceUrl+"/api/auth/FIND_ALL_USER/{id}/delete", userid)
                .headers(headers -> headers.setBearerAuth(JwtTokenStorage.getToken())) // Send JWT token
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public UserData EditUserDetails(String userid) {
        UserData user = webClient.get()
                .uri(userServiceUrl+"/api/auth/FIND_ALL_USER/{id}/edit", userid)
                .headers(headers -> headers.setBearerAuth(JwtTokenStorage.getToken())) // Send JWT token
                .retrieve()
                .bodyToMono(UserData.class)
                .block();
        return user;
    }

    public UserData registerUser(UserData userData) {
        UserData savedUser = webClient.post()
                .uri(userServiceUrl + "/api/auth/register")
                .bodyValue(userData)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class) // Extract full JSON error
                                .flatMap(errorBody -> {
                                    System.out.println("Remote Error Response: " + errorBody);
                                    return Mono.error(new RuntimeException(parseErrorMessage(errorBody)));
                                })
                )
                .bodyToMono(UserData.class)
                .block(); // Blocking to wait for the response
        return savedUser;

    }

    private String parseErrorMessage(String errorBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(errorBody);
            if (jsonNode.has("error")) {
                return jsonNode.get("error").asText(); // Extract the correct error message
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log error if JSON parsing fails
        }
        return "Unknown error occurred"; // Fallback message
    }

    public String Login(AuthRequest user) {
        String jwttoken=  webClient.post()
                .uri(userServiceUrl+"/api/auth/login")
                .bodyValue(user)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Blocking for simplicity; in production, use reactive programming
        System.out.println("00000000000000"+jwttoken);
        return jwttoken;
    }

    public void Logout(String jwtToken) {
        webClient.post()
                .uri(userServiceUrl+"/api/auth/logout")
                .header("Authorization", "Bearer " + jwtToken)
                .retrieve()
                .toBodilessEntity()
                .block(); // Blocking for simplicity; in production, use reactive programming

    }
}
