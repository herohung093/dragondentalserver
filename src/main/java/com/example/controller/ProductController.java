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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductRepo productRepo;
    @Autowired
    StaffRepo staffRepo;
    @Autowired
    InventoryRepo inventoryRepo;
    @GetMapping("/")
    public ResponseEntity getAll(){
        List<Product> products = productRepo.findAll();
        if(products== null){
            return ResponseEntity.status(404).body("Product not found !");
        }
        return ResponseEntity.ok().body(products);
    }
    @GetMapping("/color")
    public ResponseEntity getAllProductColor(){
        List<Product> products = productRepo.findAll();
        Set<String> colors = new HashSet<>();
        for(Product product: products){
            String code = product.getCode();
            String[] output = code.split("-");
            if(output.length == 3)
                colors.add(output[2]);
        }
        return ResponseEntity.ok().body(colors);
    }
    @GetMapping("/color/{color}")
    public ResponseEntity getAllByColor(@PathVariable("color") String color){
        List<Product> products = productRepo.getAllByColor("-"+color);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/size/{size}")
    public ResponseEntity getAllBySize(@PathVariable("size") String size){
        List<Product> products = productRepo.getAllBySize("-"+size+"-");
        return ResponseEntity.ok().body(products);
    }
    @GetMapping("/size")
    public ResponseEntity getAllProductSize(){
        List<Product> products = productRepo.findAll();
        Set<String> colors = new HashSet<>();
        for(Product product: products){
            String code = product.getCode();
            String[] output = code.split("-");
            if(output.length == 3)
                colors.add(output[1]);
        }
        return ResponseEntity.ok().body(colors);
    }
    @GetMapping("/{code}")
    public ResponseEntity getProductByCode(@PathVariable String code){
        Product p = productRepo.findByCode(code);
        if(p== null){
            return ResponseEntity.status(404).body("Product not found !");
        }
        return ResponseEntity.ok().body(p);
    }

    @PostMapping("/")
    public ResponseEntity addProductWithQuantity(@RequestBody Product product, @RequestBody int quantity){
        Product existing = productRepo.findByCode(product.getCode());
        System.out.println(product.toString());
        if(existing!= null){
            return ResponseEntity.status(409).body("The Product already exist.");
        }

        Inventory inventory = inventoryRepo.findByCode(product.getCode());

        if(inventory!= null){

            inventory.setStock(quantity);
            inventoryRepo.save(inventory);
            return ResponseEntity.status(409).body("The Product already exist.");
        }
        inventory = new Inventory(product.getCode(),quantity);
        //add product into 2 tables
        System.out.println(inventory.toString());
        inventoryRepo.save(inventory);
        productRepo.save(product);

        return ResponseEntity.ok().body("Product added");
    }
    @PostMapping("/add/inventory")
    public ResponseEntity addProduct(@RequestBody Product product){
        System.out.println(product.toString());
        Product existing = productRepo.findByCode(product.getCode());
        if(existing!= null){
            return ResponseEntity.status(409).body("The Product already exist.");
        }
    Product newProduct = new Product(product.getCode());
        productRepo.save(newProduct);

        return ResponseEntity.ok().body("Product added");
    }
    @PutMapping("/decrease")
    public ResponseEntity decreaseProductStock(@RequestParam("code") String code, @RequestParam("quantity") int quantity){
        Product p = productRepo.findByCode(code);
        if(p== null){
            return ResponseEntity.status(404).body("Product not found !");
        }
        inventoryRepo.decreaseQuantity(code,quantity);
        return ResponseEntity.ok().body("Product quantity has changed");
    }

    @PutMapping("/{code}/{quantity}")
    public ResponseEntity updateProductStock(@PathVariable("code") String code, @PathVariable("quantity") int quantity){
        Product p = productRepo.findByCode(code);
        if(p== null){
            return ResponseEntity.status(404).body("Product not found !");
        }
        inventoryRepo.updateStock(code,quantity);
        return ResponseEntity.ok().body("Product quantity has been updated");
    }

}
