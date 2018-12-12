package com.example.controller;

import com.example.model.Customer;
import com.example.model.Product;
import com.example.model.ProductInput;
import com.example.model.Staff;
import com.example.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static java.nio.charset.StandardCharsets.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//@CrossOrigin(origins = "https://radiant-fjord-77052.herokuapp.com", maxAge = 3600)
@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerRepo customerRepo;

    @PostMapping("/")
    public ResponseEntity addCustomer(@RequestBody Customer customer){
        Customer existing = customerRepo.findByName(customer.getName());
        if(existing!= null){
            return ResponseEntity.status(409).body("Customer Already exist.");
        }
        existing = new Customer(customer.getName(),customer.getPhone(),customer.getAddress()
                ,customer.getContactPerson(),customer.getNote());
        if( existing.getContactPerson() == null){
            existing.setContactPerson("N/A");
        }
        if( existing.getNote() == null){
            existing.setNote("N/A");
        }
        if( existing.getPhone() == null){
            existing.setPhone("N/A");
        }
        customerRepo.save(existing);
        return ResponseEntity.ok().body("Customer has been added");
    }

    @GetMapping("/{name}")
    public ResponseEntity getByName(@PathVariable String name){
        Customer customer = customerRepo.findByName(name);
        if(customer== null){
            return ResponseEntity.status(404).body("Customer not found !");
        }
        return ResponseEntity.ok().body(customer);
    }
    @GetMapping("/")
    public ResponseEntity getAll(){
        List<Customer> customers = customerRepo.findAll();
        if(customers== null){
            return ResponseEntity.status(404).body("there is none customer in database !");
        }
        return ResponseEntity.ok().body(customers);
    }

    @PutMapping("/")
    public ResponseEntity updateCustomer(@RequestBody Customer customer){
        Customer exist = customerRepo.findByName(customer.getName());
        if(exist == null)
            return ResponseEntity.badRequest().body("Customer name does not exist");
        customerRepo.updateCustomer(customer.getName(),customer.getAddress(),customer.getNote(),customer.getContactPerson(),customer.getPhone());
        return ResponseEntity.ok().body("Customer updated");
    }
   /* @GetMapping("/import")
    public ResponseEntity importData(){
        String csvfile="Customer.csv";
        String line = "";
        String cvsSplitBy = ",";
        ArrayList<Customer> customerss = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvfile))) {


            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] customers = line.split(cvsSplitBy);

                if(customers.length == 3){
                    System.out.println("customers [ten= " + customers[0] + " , sdt=" + customers[1] + "]"+ " , dia chi=" + customers[2]);
                    Customer customer = new Customer(new String(customers[0].replace("\"","")),new String(customers[1].replace("\"","")),new String(customers[2]).replace("\"",""));
                    //customerss.add(customer);
                    customerRepo.save(customer);
                }
                if(customers.length == 4){
                    System.out.println("customers [ten= " + customers[0] + " , sdt=" + customers[1] + "]"+ " , dia chi=" + customers[2]);
                    Customer customer = new Customer(new String(customers[0].replace("\"","")),new String(customers[1].replace("\"","")),new String(customers[2].replace("\"","")+", "+customers[3].replace("\"","")));
                    //customerss.add(customer);
                    customerRepo.save(customer);
                }
                if(customers.length == 5){
                    System.out.println("customers [ten= " + customers[0] + " , sdt=" + customers[1] + "]"+ " , dia chi=" + customers[2]);
                    Customer customer = new Customer(new String(customers[0].replace("\"","")),new String(customers[1].replace("\"","")),new String(customers[2].replace("\"","")+", "+customers[3].replace("\"","")+", "+customers[4].replace("\"","")));
                    //customerss.add(customer);
                    customerRepo.save(customer);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().body(customerss);
    }*/
}
