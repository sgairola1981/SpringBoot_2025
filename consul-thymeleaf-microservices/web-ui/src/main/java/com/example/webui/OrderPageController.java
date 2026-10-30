
package com.example.webui;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrderPageController {

    private final RestClient restClient;

    public OrderPageController(
            @Value("${app.gateway-url}") String gatewayUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(gatewayUrl)
                .build();
    }

    @GetMapping("/")
    public String home(Model model) {

        List<Map<String, Object>> orders =
                restClient.get()
                        .uri("/api/orders")
                        .retrieve()
                        .body(new ParameterizedTypeReference<>() {});

        model.addAttribute("orders",
                orders == null ? List.of() : orders);

        model.addAttribute("orderForm",
                new OrderForm("", BigDecimal.ZERO));

        return "orders";
    }

    @PostMapping("/orders")
    public String create(
            @RequestParam("customerName") String customerName,
            @RequestParam("amount") BigDecimal amount,
            RedirectAttributes redirectAttributes) {

        System.out.println("=================");

        if (customerName == null || customerName.isBlank()
                || amount == null || amount.signum() <= 0) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Enter a customer name and an amount greater than zero.");

            return "redirect:/";
        }

        restClient.post()
                .uri("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new OrderRequest(customerName.trim(), amount))
                .retrieve()
                .toBodilessEntity();

        redirectAttributes.addFlashAttribute(
                "success",
                "Order created successfully.");

        return "redirect:/";
    }

    public record OrderForm(
            String customerName,
            BigDecimal amount) {}

    public record OrderRequest(
            String customerName,
            BigDecimal amount) {}
}
