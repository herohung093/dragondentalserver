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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

//@CrossOrigin(origins = "https://radiant-fjord-77052.herokuapp.com", maxAge = 3600)
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

    @PostMapping("/")
    public ResponseEntity addStock(@RequestBody ProductInput productInput){
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

        return ResponseEntity.ok().body("Inventory updated. \n Total Stock: "+ productInput.getQuantity());
    }
    @PutMapping("/")
    public ResponseEntity SetStock(@RequestBody ProductInput productInput){
        Product existing = productRepo.findByCode(productInput.getProduct().getCode());
        Staff staff = staffRepo.findByName(productInput.getOperator().getName());

        inventoryRepo.updateStock(productInput.getProduct().getCode(),productInput.getQuantity());
        productInput.setDescription(productInput.getDescription() + ". Reset stock");
        productInput.setOperator(staff);
        productInput.setProduct(existing);
        productInputRepo.save(productInput);

        return ResponseEntity.ok().body("Stock has been set. \n Total Stock: "+ productInput.getQuantity());
    }

    @PutMapping("/increase")
    public ResponseEntity decreaseProductStock(@RequestBody ProductInput productInput){
        Product existing = productRepo.findByCode(productInput.getProduct().getCode());
        Staff staff = staffRepo.findByName(productInput.getOperator().getName());

        inventoryRepo.increaseQuantity(productInput.getProduct().getCode(),productInput.getQuantity());
        productInput.setDescription(productInput.getDescription() + ". Increase stock");
        productInput.setOperator(staff);
        productInput.setProduct(existing);
        productInputRepo.save(productInput);

        return ResponseEntity.ok().body("Stock has been increased. \n Total Stock: "+ productInput.getQuantity());
    }
    @PutMapping("/decrease")
    public ResponseEntity increaseProductStock(@RequestBody ProductInput productInput){
        Product existing = productRepo.findByCode(productInput.getProduct().getCode());
        Staff staff = staffRepo.findByName(productInput.getOperator().getName());

        inventoryRepo.decreaseQuantity(productInput.getProduct().getCode(),productInput.getQuantity());
        productInput.setDescription(productInput.getDescription() + ". Increase stock");
        productInput.setOperator(staff);
        productInput.setProduct(existing);
        productInputRepo.save(productInput);

        return ResponseEntity.ok().body("Stock has been decrease. \n Total Stock: "+ productInput.getQuantity());
    }
    @GetMapping("/color/{color}")
    public ResponseEntity getAllByColor(@PathVariable("color") String color){
        List<ProductInput> products = productInputRepo.getAllByColor("-"+color);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/size/{size}")
    public ResponseEntity getAllBySize(@PathVariable("size") String size) {
        List<ProductInput> productInputs = productInputRepo.getAllByColor("-"+size+"-");
        return ResponseEntity.ok().body(productInputs);
    }

    @GetMapping("/date/between")
    public ResponseEntity getOrderBetween(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        return ResponseEntity.ok().body(productInputRepo.getAllByDate(start,end));

    }

    @GetMapping("/{code}")
    public ResponseEntity getAllByCode(@PathVariable("code") String code) {
        List<ProductInput> productInputs = productInputRepo.getAllByCode(code);
        return ResponseEntity.ok().body(productInputs);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity deleteByCode(@PathVariable("code") Long id){
        ProductInput productInput = productInputRepo.getById(id);
        if(productInput == null )
            return ResponseEntity.badRequest().body("Product Input does not exist!");

        productInputRepo.deleteById(id);
        inventoryRepo.decreaseQuantity(productInput.getProduct().getCode(),productInput.getQuantity());

        return ResponseEntity.ok().body("Product input deleted. current stock of Product: "
                +inventoryRepo.findByCode(productInput.getProduct().getCode()).getStock());
    }

    @GetMapping("/")
    public ResponseEntity getAll(){
        return ResponseEntity.ok().body(productInputRepo.findAll());
    }
}
