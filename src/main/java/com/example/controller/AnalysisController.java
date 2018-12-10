package com.example.controller;

import com.example.repository.InventoryRepo;
import com.example.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

//@CrossOrigin(origins = "https://radiant-fjord-77052.herokuapp.com", maxAge = 3600)
@RestController
@RequestMapping("/analysis")
public class AnalysisController {
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private InventoryRepo inventoryRepo;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @GetMapping("/topcustomer")
    public ResponseEntity getTopCustomer(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        return ResponseEntity.ok().body(orderRepo.getTopCustomer(start,end));
    }

    @GetMapping("/bestseller")
    public ResponseEntity getBestseller(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        return ResponseEntity.ok().body(orderRepo.getBestSeller(start,end));
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

    @GetMapping("/dept")
    public ResponseEntity getDepters(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){

        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        return ResponseEntity.ok().body(orderRepo.getDebter(start, end));
    }

    @GetMapping("/dept/{id}")//get all dept of a customer by id
    public ResponseEntity getDeptById(@PathVariable("id") Long id){
        float paid = orderRepo.getTotalPaidById(id);
        float toPay = orderRepo.getTotalToPayById(id);
        return ResponseEntity.ok().body(toPay-paid);
    }

    @GetMapping("/soldproduct/{productCode}")
    public ResponseEntity getSoldQuantity(@PathVariable("productCode") String productCode,
                                          @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        return ResponseEntity.ok().body(orderRepo.getSoldQuantity(productCode,start,end));
    }
}
