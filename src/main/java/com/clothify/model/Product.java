package com.clothify.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private int productId;
    private String productCode;
    private String name;
    private double unitPrice;
    private int quantity;
    private boolean active;
}
