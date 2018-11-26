package com.example.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Staff {
    @Id
    String name;
    String mobilePhone;
    String address;
    String password;
    public Staff() {
    }

    public Staff(String name, String mobilePhone, String address) {
        this.name = name;
        this.mobilePhone = mobilePhone;
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Staff(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "name='" + name + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
