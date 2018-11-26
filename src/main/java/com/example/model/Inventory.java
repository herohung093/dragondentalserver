package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Inventory {
    @Id
    private String code;
    private int stock;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "code", nullable = false)
    private Product product;
    public Inventory() {
    }

    public Inventory(String code, int stock) {
        this.code = code;
        this.stock = stock;
    }

    public Inventory(String code) {
        this.code = code;
        this.stock = 0;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "code='" + code + '\'' +
                ", stock=" + stock +
                '}';
    }
}
