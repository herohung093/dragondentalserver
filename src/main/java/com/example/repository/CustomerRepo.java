package com.example.repository;

import com.example.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    Customer save(Customer customer);
    Customer findByName(String name);
    Customer findById (long id);
    @Override
    List<Customer> findAll();
}
