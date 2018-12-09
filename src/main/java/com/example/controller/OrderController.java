package com.example.controller;

import com.example.model.*;
import com.example.repository.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

//@CrossOrigin(origins = "https://radiant-fjord-77052.herokuapp.com", maxAge = 3600)
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
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    Logger logger = LoggerFactory.getLogger(OrderController.class);
    @GetMapping("/")
    public ResponseEntity getAll(){

        return ResponseEntity.ok().body(orderRepo.getAllOrder());

    }
    @GetMapping("/normal")
    public ResponseEntity getAllNormal(){

        return ResponseEntity.ok().body(orderRepo.getAllNormalOrder());

    }
    @GetMapping("/instalment")
    public ResponseEntity getAllInstalment(){

        return ResponseEntity.ok().body(orderRepo.getALlInstalment());

    }
    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){

        return ResponseEntity.ok().body(orderRepo.retrieveById(id));

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
    public ResponseEntity getOrderByDate(@RequestParam("date") String date){
        LocalDate localDate = LocalDate.parse(date, formatter);

        return ResponseEntity.ok().body(orderRepo.findAllByCreateAtAfter(localDate));
    }
    @GetMapping("/date/between")
    public ResponseEntity getOrderBetween(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        return ResponseEntity.ok().body(orderRepo.getAllByDate(start,end));

    }
    @PostMapping("/")
    public ResponseEntity createOrder(@RequestBody Order order){

        Staff staff = staffRepo.findByName(order.getStaff().getName());
        if( staff == null){
            return ResponseEntity.status(400).body("Staff not found. Bad request");
        }
        Customer customer = customerRepo.findByName(order.getCustomer().getName());
        if( customer == null){
            return ResponseEntity.status(400).body("Customer not found. Bad request");
        }
        List<OrderLine> orderLines = new ArrayList<>();
                orderLines.addAll(order.getOrderLines());
        if( orderLines == null){
            return ResponseEntity.status(400).body("there is no item to create order. Bad request");
        }
        Order newOrder = new Order(customer,staff);
        newOrder.setPaid(order.getPaid());
        newOrder.setNote(order.getNote());
        newOrder.setIsInstalment(order.isInstalment());
        //orderRepo.save(newOrder);
        for(OrderLine orderLine: orderLines){
            orderLine.setOrder(newOrder);
            newOrder.getOrderLines().add(orderLine);
            int quantity = inventoryRepo.findByCode(orderLine.getProduct().getCode()).getStock();
            if(orderLine.getQuantity()> quantity){
                return ResponseEntity.status(400).body("Not enough stock in store for product: "+ orderLine.getProduct().getCode()+"/n"
                    +"Stock in store: "+quantity+"/n"
                    +"Request: "+orderLine.getQuantity());
            }
            inventoryRepo.decreaseQuantity(orderLine.getProduct().getCode(),orderLine.getQuantity());
        }

        order.setOrderLines(orderLines);
        orderRepo.save(newOrder);
        return ResponseEntity.ok().body("Order Created");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOrder(@PathVariable Long id){
        Order oldOrder = orderRepo.getById(id);
        if(oldOrder == null)
            return ResponseEntity.status(404).body("Order Id not found");
        List<OrderLine> orderLines = oldOrder.getOrderLines();
        for(OrderLine orderLine: orderLines){
            inventoryRepo.increaseQuantity(orderLine.getProduct().getCode(),orderLine.getQuantity());
        }
        orderRepo.deleteOrderLines(id);
        orderRepo.deleteById(id);
        return ResponseEntity.ok().body("Order: "+id+" deleted");
    }

    @PutMapping("/")
    public ResponseEntity updateOrder(@RequestBody Order newOrder){
        Order oldOrder = orderRepo.getById(newOrder.getId());
        if(oldOrder == null)
            return ResponseEntity.status(404).body("Order Id not found");
        List<OrderLine> oldOrderLines = oldOrder.getOrderLines();
        if(oldOrderLines.size()>0){
            orderRepo.deleteOrderLines(oldOrder.getId());
            for(OrderLine orderLine: oldOrderLines){

                inventoryRepo.increaseQuantity(orderLine.getProduct().getCode(),orderLine.getQuantity());
            }
        }
        oldOrderLines.clear();
        orderRepo.updateOrder(oldOrder.getId(),newOrder.getPaid(), newOrder.getNote(),newOrder.isInstalment());
        List<OrderLine> newOrderLines = new ArrayList<>();
        newOrderLines.addAll(newOrder.getOrderLines());

        if(newOrderLines.size()>0){
            for(OrderLine orderLine: newOrderLines){
                orderLine.setOrder(oldOrder);
                oldOrder.getOrderLines().add(orderLine);
                int quantity = inventoryRepo.findByCode(orderLine.getProduct().getCode()).getStock();
                if(orderLine.getQuantity()> quantity){
                    return ResponseEntity.status(400).body("Not enough stock in store for product: "+ orderLine.getProduct().getCode()+"/n"
                            +"Stock in store: "+quantity+"/n"
                            +"Request: "+orderLine.getQuantity());
                }
                orderRepo.updateOrderItem(oldOrder.getId(),orderLine.getProduct().getCode(),orderLine.getPrice(),orderLine.getQuantity(),orderLine.getDiscount(),orderLine.getTotalPrice());
                inventoryRepo.decreaseQuantity(orderLine.getProduct().getCode(),orderLine.getQuantity());
            }
        }
        return ResponseEntity.ok().body("Order Updated");
    }
    @GetMapping("/customer/{id}")
    public ResponseEntity getOrderByCustomer(@PathVariable Long id){
        return ResponseEntity.ok().body(orderRepo.getOrderByCustomer(id));
    }
}
