package com.example.controller;

import com.example.model.Staff;
import com.example.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@CrossOrigin(origins = "https://radiant-fjord-77052.herokuapp.com", maxAge = 3600)
@RestController
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    StaffRepo staffRepo;


    @GetMapping("/{name}")
    public ResponseEntity getByName(@PathVariable String name){
        Staff staff = staffRepo.findByName(name);
        if(staff== null){
            return ResponseEntity.status(404).body("Staff not found !");
        }
        return ResponseEntity.ok().body(staff);
    }

    @GetMapping("/")
    public ResponseEntity getAll(){
        List<Staff> staff = staffRepo.findAll();
        if(staff== null){
            return ResponseEntity.status(404).body("Product not found !");
        }
        return ResponseEntity.ok().body(staff);
    }

    @PostMapping("/")
    public ResponseEntity addStaff(@RequestBody Staff staff){
        Staff exist = staffRepo.findByName(staff.getName());
        if(exist!= null){
            return ResponseEntity.status(400).body("Staff already exist !");
        }
        if(staff.getMobilePhone().equals("") || staff.getMobilePhone()==null) {
            staff.setMobilePhone("N/A");
        }
        if(staff.getAddress().equals("") || staff.getAddress()==null) {
            staff.setAddress("N/A");
        }

        staffRepo.save(staff);
        return ResponseEntity.ok().body(staff);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity updateStaff(@PathVariable String name){
        Staff exist = staffRepo.findByName(name);
        if(exist== null)
           return ResponseEntity.ok().body("Staff name not found");
        staffRepo.deleteByName(name);
        return ResponseEntity.ok().body("Staff Deleted: ");
    }
}
