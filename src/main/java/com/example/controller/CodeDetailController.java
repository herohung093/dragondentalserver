package com.example.controller;

import com.example.model.CodeDetail;
import com.example.repository.CodeDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/codedetail")
public class CodeDetailController {
    @Autowired
    CodeDetailRepo codeDetailRepo;

    @GetMapping("/names")
    public ResponseEntity getAllProductNames(){
        return ResponseEntity.ok().body(codeDetailRepo.findAllProductByType("name"));
    }
    @GetMapping("/sizes")
    public ResponseEntity getAllProductSizes(){
        return ResponseEntity.ok().body(codeDetailRepo.findAllProductByType("size"));
    }
    @GetMapping("/colors")
    public ResponseEntity getAllProductColors(){
        return ResponseEntity.ok().body(codeDetailRepo.findAllProductByType("color"));
    }

    public boolean checkNameExist(String name){
        CodeDetail exist = codeDetailRepo.checkExist(name,"name");
        if (exist == null)
            return false;
        codeDetailRepo.save(new CodeDetail(name,"name"));
        return true;
    }
    public boolean checkSizeExist(String size){
        CodeDetail exist = codeDetailRepo.checkExist(size,"size");
        if (exist == null)
            return false;
        codeDetailRepo.save(new CodeDetail(size,"size"));
        return true;
    }
    public boolean checkColorExist(String color){
        CodeDetail exist = codeDetailRepo.checkExist(color,"color");
        if (exist == null)
            return false;
        codeDetailRepo.save(new CodeDetail(color,"color"));
        return true;
    }
}
