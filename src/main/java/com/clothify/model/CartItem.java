package com.clothify.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Product product;
    private int quantity;
    private double lineTotal;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.lineTotal = product.getUnitPrice() * quantity;
    }

    public void addQuantity(int qty) {
        this.quantity += qty;
        this.lineTotal = product.getUnitPrice() * this.quantity;
    }

    public String getProductName() {
        return product.getName();
    }

    public double getUnitPrice() {
        return product.getUnitPrice();
    }

    public int getProductId() {
        return product.getProductId();
    }

    public String getProductCode() {
        return product.getProductCode();
    }
}

