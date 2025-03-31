package com.vayam.firstservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String orderId;
    private String productName;
    private int quantity;

    public Order(String orderId) {
        this.orderId = orderId;
    }
}
