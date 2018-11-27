package com.example.controller;

import com.example.repository.InventoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryRepo inventoryRepo;
    @GetMapping("/")
    public ResponseEntity getAll(){

        return ResponseEntity.ok().body(inventoryRepo.findAll());

    }
}
