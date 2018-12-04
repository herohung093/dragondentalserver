package com.example.repository;

import com.example.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    Customer save(Customer customer);
    Customer findByName(String name);
    Customer findById (long id);
    @Override
    List<Customer> findAll();
    @Query("update Customer c set  c.name = :name, c.address = :address, c.note = :note, c.contactPerson = :contactPerson, c.phone = :phone ")
    void updateCustomer(@Param("name")String name, @Param("address")String address
            ,@Param("note")String note,@Param("contactPerson")String contacperson,@Param("phone")String phone);
}
