package com.example.model.Interface;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public interface Debter {
    String getCustomer();
    float getTotal();
    float getPaid();
    Long getOrderId();
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate getDate();

}
