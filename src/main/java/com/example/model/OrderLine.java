package com.example.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class OrderLine implements Serializable{
    @Id
    @ManyToOne
    @JoinColumn(name = "product_code")
    private Product product;
    @Id
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="EVENT_SEQ")
    @SequenceGenerator(name="EVENT_SEQ", sequenceName="EVENT_SEQ", allocationSize=20)
    private long id;
    private int quantity;
    private float price;
    private int discount;
    private float totalPrice;

    private void calculateTotalPrice(){
        totalPrice=((quantity * price)*(100-discount))/100;
    }
    public float getTotalPrice() {
        calculateTotalPrice();
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
    public OrderLine(Product product, Order order, int quantity, float price, int discount) {
        this.product = product;
        this.order = order;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        getTotalPrice();
    }
    public OrderLine(String product, Order order, int quantity, float price, int discount) {
        this.product = new Product(product);
        this.order = order;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        getTotalPrice();
    }
    public OrderLine(Product product, int quantity, float price, int discount) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        getTotalPrice();
    }
    public OrderLine(String productCode, int quantity, float price, int discount){
        this.product = new Product(productCode);
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        getTotalPrice();
    }
    public OrderLine() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        //getTotalPrice();
        this.order = order;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderLine{" +
                "product=" + product.getCode() +
                ", order=" + //order +
                ", quantity=" + quantity +
                ", price=" + price +
                ", discount=" + discount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderLine)) return false;
        OrderLine orderLine = (OrderLine) o;
        return getQuantity() == orderLine.getQuantity() &&
                Float.compare(orderLine.getId(), getId()) == 0 &&
                (getProduct().getCode().equals(orderLine.getProduct().getCode()) ) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProduct(), getOrder(), getQuantity(), getPrice());
    }
}
