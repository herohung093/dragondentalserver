package com.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},allowGetters = true)
        public class ProductInput {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn(name = "code")
    private Product product;
    @CreationTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createAt;
    @UpdateTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate updateAt;

    private String description;

    private int quantity;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "staff")
    private Staff operator;

    public ProductInput() {
    }

    public ProductInput(Product product, String description, int quantity, Staff operator) {
        this.product = product;
        this.description = description;
        this.quantity = quantity;
        this.operator = operator;
    }

    public long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public LocalDate getUpdateAt() {
        return updateAt;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public Staff getOperator() {
        return operator;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setOperator(Staff operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "ProductInput{" +
                "id=" + id +
                ", product=" + product +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", operator=" + operator +
                '}';
    }
}
