package com.example.model.Interface;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public interface ResponseOrder {
    Long getId();
    String getCustomer();
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate getCreateAt();
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate getUpdateAt();
    Float getPaid();
    String getNote();
    Boolean getIsInstalment();
    String getStaff();
}