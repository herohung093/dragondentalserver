package com.example.controller;

import com.example.model.ApplicationUser;
import com.example.repository.ApplicationUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private ApplicationUserRepo applicationUserRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody ApplicationUser user) {
        ApplicationUser existing = applicationUserRepo.findByUsername(user.getUsername());

        if (existing != null) {
            return ResponseEntity.badRequest().body("That username is already taken.");
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        applicationUserRepo.save(user);

        return ResponseEntity.ok().body("Account created successfully!");
    }
    @GetMapping("/reset-password/{password}")
    public  ResponseEntity resetPassword(@PathVariable String password){
        //extract user name
        String username =  SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal().toString();
        ApplicationUser applicationUser = applicationUserRepo.findByUsername(username);
        applicationUser.setPassword(bCryptPasswordEncoder.encode(password));
        applicationUserRepo.save(applicationUser);
        return ResponseEntity.ok().body("Password changed successfully!");
    }
}
