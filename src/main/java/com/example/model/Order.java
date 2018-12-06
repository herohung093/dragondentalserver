package com.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Entity
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},allowGetters = true)
@Table(name = "order_")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer")
    private Customer customer;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "staff")
    private Staff staff;
    @CreationTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createAt;
    @UpdateTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate updateAt;
    private String note;
    private float paid = 0;
    private boolean isInstalment;




    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderLine> orderLines = new ArrayList<>();

    public Order(Customer customer, Staff staff) {
        this.customer = customer;
        this.staff = staff;
        this.isInstalment = false;
    }

    public float getPaid() {
        return paid;
    }

    public void setPaid(float paid) {
        this.paid = paid;
    }

    public boolean isInstalment() {
        return isInstalment;
    }

    public void setInstalment(boolean instalment) {
        isInstalment = instalment;
    }

    public Order() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public LocalDate getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;

    }
    public void addProduct(Product product, int quantity, float price, int discount){
        OrderLine orderLine = new OrderLine(product.getCode(), this, quantity, price, discount);
        orderLines.add(orderLine);
        product.getOrderLines().add(orderLine);
    }

    public void removeProduct(Product product){
        for(Iterator<OrderLine> iterator = orderLines.iterator();
        iterator.hasNext();){
            OrderLine orderLine = iterator.next();
            if(orderLine.getOrder().equals(this) &&
                orderLine.getProduct().equals(product)){
                iterator.remove();
                orderLine.getOrder().getOrderLines().remove(orderLine);
                orderLine.setOrder(null);
                orderLine.setProduct(null);
            }
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer=" + customer.getContactPerson() +
                ", staff=" + staff.getName() +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                ", note='" + note + '\'' +
                ", paid=" + paid +
                ", isInstalment=" + isInstalment +
                ", orderLines=" + orderLines +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getId() == order.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
