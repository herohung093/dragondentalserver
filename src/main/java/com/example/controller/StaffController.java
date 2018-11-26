package com.example.controller;

import com.example.model.Product;
import com.example.model.Staff;
import com.example.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    StaffRepo staffRepo;

    @GetMapping("/get/{name}")
    public ResponseEntity getByName(@PathVariable String name){
        Staff staff = staffRepo.findByName(name);
        if(staff== null){
            return ResponseEntity.status(404).body("Staff not found !");
        }
        return ResponseEntity.ok().body(staff);
    }

    @GetMapping("/get")
    public ResponseEntity getAll(){
        List<Staff> staff = staffRepo.findAll();
        if(staff== null){
            return ResponseEntity.status(404).body("Product not found !");
        }
        return ResponseEntity.ok().body(staff);
    }
}
