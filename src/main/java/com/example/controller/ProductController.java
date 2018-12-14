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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@CrossOrigin(origins = "https://radiant-fjord-77052.herokuapp.com", maxAge = 3600)
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductRepo productRepo;
    @Autowired
    StaffRepo staffRepo;
    @Autowired
    InventoryRepo inventoryRepo;
    @Autowired
    ProductInputRepo productInputRepo;
    @Autowired
    CodeDetailController codeDetailController;

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
    @GetMapping("/name")
    public ResponseEntity getAllProductName(){
        List<Product> products = productRepo.findAll();
        Set<String> names = new HashSet<>();
        for(Product product: products){
            String name = product.getCode();
            String[] output = name.split("-");
            if(output.length == 3)
                names.add(output[0]);
        }
        return ResponseEntity.ok().body(names);
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
    public ResponseEntity addProductWithQuantity(@RequestBody ProductInput productInput){
        Product existing = productRepo.findByCode(productInput.getProduct().getName());
        System.out.println(productInput.toString());
        if(existing!= null){
            return ResponseEntity.status(409).body("The Product already exist.");
        }
        //add productInput into 2 tables

        Staff staff  = staffRepo.findByName(productInput.getOperator().getName());
        Product product = productInput.getProduct();
        if(product.getUnit().equals(""))
            product.setUnit("N/A");

        productInput.setOperator(staff);

        productRepo.save(product);
        productInputRepo.save(productInput);
        inventoryRepo.save(new Inventory(productInput.getProduct().getCode(),productInput.getQuantity()));
        return ResponseEntity.ok().body("Product added");
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
//TODO write update API for product
}
