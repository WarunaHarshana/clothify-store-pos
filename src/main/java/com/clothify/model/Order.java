package com.clothify.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int orderId;
    private String orderDate;
    private int cashierUserId;
    private double totalAmount;
    private String status;
}

