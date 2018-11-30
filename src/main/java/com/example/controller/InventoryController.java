package com.example.controller;

import com.example.model.Inventory;
import com.example.repository.InventoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryRepo inventoryRepo;
    @GetMapping("/")
    public ResponseEntity getAll(){

        return ResponseEntity.ok().body(inventoryRepo.findAll());

    }
    @GetMapping("/color/{color}")
    public ResponseEntity getAllByColor(@PathVariable("color") String color) {
        List<Inventory> inventories = inventoryRepo.getAllByColor("-"+color);
        return ResponseEntity.ok().body(inventories);
    }
    @GetMapping("/size/{size}")
    public ResponseEntity getAllBySize(@PathVariable("size") String size) {
        List<Inventory> inventories = inventoryRepo.getAllByColor("-"+size+"-");
        return ResponseEntity.ok().body(inventories);
    }
}
