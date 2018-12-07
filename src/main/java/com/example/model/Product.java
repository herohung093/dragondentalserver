package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Product  {
    @Id
    private String code;
    private String name;
    private Float price;
    private String unit;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "product")
    private Inventory inventory;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderLine> orderLines;
    public Product(String code, String name, float price, String unit) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.unit = unit;
    }


    public Product(String code, float price, String unit) {
        this.code = code;
        this.price = price;
        this.unit = unit;
    }

    public Product(String code) {
        this.code = code;
        this.price = Float.valueOf(0);
    }
    public Product(String code, String name) {
        this.code = code;
        this.price = Float.valueOf(0);
    }

    public Product() {}

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Product{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", unit='" + unit + '\'' +
                '}';
    }

    @JsonIgnore
    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return this.getCode().equals(product.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }
}
