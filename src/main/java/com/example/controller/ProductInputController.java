package com.example.controller;


import com.example.model.Inventory;
import com.example.model.Product;
import com.example.model.ProductInput;
import com.example.model.Staff;
import com.example.repository.InventoryRepo;
import com.example.repository.ProductInputRepo;
import com.example.repository.ProductRepo;
import com.example.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productinput")
public class ProductInputController {
    @Autowired
    ProductInputRepo productInputRepo;
    @Autowired
    StaffRepo staffRepo;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    InventoryRepo inventoryRepo;
/*    @GetMapping("/productinput")
     void updateProductInput(@RequestParam("product") Product p, @RequestParam("quantity") int quantity, @RequestParam("staff") Staff s, @RequestParam("description") String des){
        //staffRepo.save(s);
        ProductInput pi= new ProductInput(p,des,quantity,s);
        ProductInput a = productInputRepo.save(pi);
        System.out.println("A: "+a.toString());
    }*/

    @PostMapping("/add")
    public ResponseEntity increaseStock(@RequestBody ProductInput productInput){
        Product existing = productRepo.findByCode(productInput.getProduct().getCode());
        Staff staff = staffRepo.findByName(productInput.getOperator().getName());
        if(existing== null){
            return ResponseEntity.status(404).body("Product not found. Need to add product first");
        }

        if(staff== null){
            return ResponseEntity.status(404).body("Staff not found. Need to add staff first");
        }
        inventoryRepo.increaseQuantity(productInput.getProduct().getCode(),productInput.getQuantity());
        productInputRepo.save(productInput);

        return ResponseEntity.ok().body("Product - Input added. /n Total Stock: "+ inventoryRepo.findByCode(productInput.getProduct().getCode()));
    }
}
