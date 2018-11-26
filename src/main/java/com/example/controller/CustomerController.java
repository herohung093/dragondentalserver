package com.example.controller;

import com.example.model.Customer;
import com.example.model.Product;
import com.example.model.ProductInput;
import com.example.model.Staff;
import com.example.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerRepo customerRepo;

    @PostMapping("/add")
    public ResponseEntity addCustomer(@RequestBody Customer customer){
        Customer existing = customerRepo.findByName(customer.getName());
        if(existing!= null){
            return ResponseEntity.status(409).body("Customer Already exist.");
        }
        customerRepo.save(customer);
        return ResponseEntity.ok().body("Customer has been added");
    }

    @GetMapping("/get/{name}")
    public ResponseEntity getByName(@PathVariable String name){
        Customer customer = customerRepo.findByName(name);
        if(customer== null){
            return ResponseEntity.status(404).body("Staff not found !");
        }
        return ResponseEntity.ok().body(customer);
    }
    @GetMapping("/get")
    public ResponseEntity getAll(){
        List<Customer> customers = customerRepo.findAll();
        if(customers== null){
            return ResponseEntity.status(404).body("Product not found !");
        }
        return ResponseEntity.ok().body(customers);
    }

}
