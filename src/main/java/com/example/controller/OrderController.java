package com.example.controller;

import com.example.model.*;
import com.example.repository.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){

        return ResponseEntity.ok().body(orderRepo.getById(id));

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
        List<OrderLine> orderLines = order.getOrderLines();
        if(order == null || orderLines == null){
            return ResponseEntity.status(400).body("Order or OrderLines is null. Bad request");
        }

        //Order order = new Order();
        for(OrderLine orderLine: orderLines){
            orderLine.setOrder(order);
            //order.getOrderLines().add(orderLine);
            int quantity = inventoryRepo.findByCode(orderLine.getProduct().getCode()).getStock();
            if(orderLine.getQuantity()> quantity){
                return ResponseEntity.status(400).body("Not enough stock in store for product: "+ orderLine.getProduct().getCode()+"/n"
                    +"Stock in store: "+quantity+"/n"
                    +"Request: "+orderLine.getQuantity());
            }
            inventoryRepo.decreaseQuantity(orderLine.getProduct().getCode(),orderLine.getQuantity());
        }
        orderRepo.save(order);
        return ResponseEntity.ok().body("Order has been update");
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
        for(OrderLine orderLine: oldOrderLines){

            inventoryRepo.increaseQuantity(orderLine.getProduct().getCode(),orderLine.getQuantity());
        }
        orderRepo.deleteOrderLines(oldOrder.getId());
        oldOrderLines.clear();
        oldOrder.setCustomer(newOrder.getCustomer());
        oldOrder.setStaff(newOrder.getStaff());
        oldOrder.setInstalment(newOrder.isInstalment());
        oldOrder.setNote(newOrder.getNote());
        oldOrder.setPaid(newOrder.getPaid());

        List<OrderLine> newOrderLines = newOrder.getOrderLines();

        for(OrderLine orderLine: newOrderLines){
            orderLine.setOrder(oldOrder);
            //order.getOrderLines().add(orderLine);
            int quantity = inventoryRepo.findByCode(orderLine.getProduct().getCode()).getStock();
            if(orderLine.getQuantity()> quantity){
                return ResponseEntity.status(400).body("Not enough stock in store for product: "+ orderLine.getProduct().getCode()+"/n"
                        +"Stock in store: "+quantity+"/n"
                        +"Request: "+orderLine.getQuantity());
            }
            oldOrder.getOrderLines().add(orderLine);
            inventoryRepo.decreaseQuantity(orderLine.getProduct().getCode(),orderLine.getQuantity());
        }
        orderRepo.save(oldOrder);
        return ResponseEntity.ok().body(oldOrder);
    }

    @GetMapping("/topcustomer")
    public ResponseEntity getTopCustomer(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        return ResponseEntity.ok().body(orderRepo.getTopCustomer(start,end));
    }

    @GetMapping("/bestseller")
    public ResponseEntity getBestseller(){
        return ResponseEntity.ok().body(orderRepo.getBestSeller());
    }

    @GetMapping("/unpaid")
    public ResponseEntity getUnpaidOrder(){
        return ResponseEntity.ok().body(orderRepo.getUnpaidOrder());
    }

    @GetMapping("/income")
    public ResponseEntity getIncomeByTime(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        Float income = orderRepo.getIncomeByTime(start,end);

        if(income == null)
            return ResponseEntity.ok().body(0);
        return ResponseEntity.ok().body(income);
    }
    @GetMapping("/income/{year}")
    public ResponseEntity getIncomeOfMonths(@PathVariable("year") int year){
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = 12;
        if(year == currentYear)
             currentMonth = currentDate.getMonth().getValue();
        HashMap<Integer, Float> values = new HashMap<>();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.US);

        for (int i = 1;i <= currentMonth;i++){
            String monthString =""+i;
            if(i<10)
                monthString = "0"+i;
            String startDateString = "01/"+monthString+"/"+year;
            LocalDate startDate = LocalDate.parse(startDateString, dateFormat);
            LocalDate endDate = startDate.withDayOfMonth(startDate.getMonth().length(startDate.isLeapYear()));
            Float income = orderRepo.getIncomeByTime(startDate,endDate);
            if(income ==null)
                income = Float.valueOf(0);
            values.put(i,income);
        }
        return ResponseEntity.ok().body(values);

    }

    @GetMapping("/soldproduct/{productCode}")
    public ResponseEntity getSoldQuantity(@PathVariable("productCode") String productCode,
                                          @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        return ResponseEntity.ok().body(orderRepo.getSoldQuantity(productCode,start,end));
    }

    @GetMapping("/dept")
    public ResponseEntity getDepters(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){

        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        return ResponseEntity.ok().body(orderRepo.getDebter(start, end));
    }

    @GetMapping("/dept/{id}")
    public ResponseEntity getDeptById(@PathVariable("id") Long id){
        float paid = orderRepo.getTotalPaidById(id);
        float toPay = orderRepo.getTotalToPayById(id);
        return ResponseEntity.ok().body(toPay-paid);
    }

}
