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
    private Integer categoryId;
    private String categoryName;

    public Product(int productId, String productCode, String name, double unitPrice, int quantity, boolean active) {
        this.productId = productId;
        this.productCode = productCode;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.active = active;
    }
}
