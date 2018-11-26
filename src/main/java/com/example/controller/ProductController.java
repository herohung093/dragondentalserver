package com.example.controller;


import com.example.model.Inventory;
import com.example.model.Product;
import com.example.model.Staff;
import com.example.repository.InventoryRepo;
import com.example.repository.ProductRepo;
import com.example.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductRepo productRepo;
    @Autowired
    StaffRepo staffRepo;
    @Autowired
    InventoryRepo inventoryRepo;
    @GetMapping("/get")
    public ResponseEntity getAll(){
        List<Product> products = productRepo.findAll();
        if(products== null){
            return ResponseEntity.status(404).body("Product not found !");
        }
        return ResponseEntity.ok().body(products);
    }
    @GetMapping("/get/{code}")
    public ResponseEntity getProductByCode(@PathVariable String code){
        Product p = productRepo.findByCode(code);
        if(p== null){
            return ResponseEntity.status(404).body("Product not found !");
        }
        return ResponseEntity.ok().body(p);
    }

    @PostMapping("/add")
    public ResponseEntity addProductWithQuantity(@RequestBody Product product, @RequestBody int quantity){
        Product existing = productRepo.findByCode(product.getCode());
        if(existing!= null){
            return ResponseEntity.status(409).body("The Product already exist.");
        }
        //add product into 2 tables
        inventoryRepo.save(new Inventory(product.getCode(),quantity));
        productRepo.save(product);

        return ResponseEntity.ok().body("Product added");
    }
    @PostMapping("/add/inventory")
    public ResponseEntity addProduct(@RequestBody Inventory inventory){
        Inventory existing = inventoryRepo.findByCode(inventory.getCode());
        if(existing!= null){
            return ResponseEntity.status(409).body("The Product already exist.");
        }

        inventoryRepo.save(inventory);

        return ResponseEntity.ok().body("Product added");
    }
    @PutMapping("/update/decrease")
    public ResponseEntity decreaseProductStock(@RequestParam("code") String code, @RequestParam("quantity") int quantity){
        Product p = productRepo.findByCode(code);
        if(p== null){
            return ResponseEntity.status(404).body("Product not found !");
        }
        inventoryRepo.decreaseQuantity(code,quantity);
        return ResponseEntity.ok().body("Product quantity has changed");
    }

    @PutMapping("/update")
    public ResponseEntity updateProductStock(@RequestParam("code") String code, @RequestParam("quantity") int quantity){
        Product p = productRepo.findByCode(code);
        if(p== null){
            return ResponseEntity.status(404).body("Product not found !");
        }
        inventoryRepo.updateStock(code,quantity);
        return ResponseEntity.ok().body("Product quantity has been updated");
    }

}
