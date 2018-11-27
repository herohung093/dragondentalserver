package com.example.controller;

import com.example.model.*;
import com.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private StaffRepo staffRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private InventoryRepo inventoryRepo;
    @GetMapping("/")
    public ResponseEntity getAll(){

        return ResponseEntity.ok().body(orderRepo.findAll());

    }
    @GetMapping("/normal")
    public ResponseEntity getAllNormal(){

        return ResponseEntity.ok().body(orderRepo.getAllNormalOrder());

    }
    @GetMapping("/instalment")
    public ResponseEntity getAllInstalment(){

        return ResponseEntity.ok().body(orderRepo.getALlInstalment());

    }
    @GetMapping("/{id}/items")
    public ResponseEntity getOrderItems(@PathVariable Long id){
        List<OrderLine> p = orderRepo.getOrderItemsById(Long.valueOf(id));
        if(p== null){
            return ResponseEntity.status(404).body("Order not found !");
        }
        return ResponseEntity.ok().body(p);

    }
    @GetMapping("/date")
    public ResponseEntity getOrderByDate(@RequestBody LocalDate localDate){

        return ResponseEntity.ok().body(orderRepo.findAllByCreateAtAfter(localDate));

    }
    /*@GetMapping("/date/{localDate}")
    public ResponseEntity getOrderByDate(@PathVariable LocalDate localDate){

        return ResponseEntity.ok().body(orderRepo.findAllByCreateAtAfter(localDate));

    }*/
    @PostMapping("/")
    public ResponseEntity createOrder(@RequestBody Order order, @RequestBody List<OrderLine> orderLines){
        if(order == null || orderLines == null){
            return ResponseEntity.status(400).body("Order or OrderLines is null. Bad request");
        }

        //Order order = new Order();
        for(OrderLine orderLine: orderLines){
            orderLine.setOrder(order);
            order.getOrderLines().add(orderLine);
            if(orderLine.getQuantity()> inventoryRepo.findByCode(orderLine.getProduct().getCode()).getStock()){
                return ResponseEntity.status(400).body("Not enough stock in store for product: "+ orderLine.getProduct().getCode()+"/n"
                    +"Stock in store: "+inventoryRepo.findByCode(orderLine.getProduct().getCode()).getStock()+"/n"
                    +"Request: "+orderLine.getQuantity());
            }
            inventoryRepo.decreaseQuantity(orderLine.getProduct().getCode(),orderLine.getQuantity());
        }
        orderRepo.save(order);
        return ResponseEntity.ok().body(order);
    }


/*
//example of how to save an order
    @GetMapping("/example")
    public void addSample(){

        Staff staff = staffRepo.findByName("Hoang");
        Customer customer = customerRepo.findById(4);
        Order order = new Order(customer,staff);
        List<OrderLine> orderLines = new ArrayList<>();
        orderLines.add(new OrderLine("NEW-CX",5,33000));


        addOrder(order,orderLines);

    }
    //testing
    public void addOrder(@RequestBody Order order, @RequestBody List<OrderLine> orderLines){

        //Order order = new Order();
        for(OrderLine orderLine: orderLines){
            orderLine.setOrder(order);
            order.getOrderLines().add(orderLine);
            inventoryRepo.decreaseQuantity(orderLine.getProduct().getCode(),orderLine.getQuantity());
        }
        order.calculateTotalPrice();
        orderRepo.save(order);

    }
*/


}
