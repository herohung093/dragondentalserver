package com.example.repository;

import com.example.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserRepo extends JpaRepository<ApplicationUser, Long> {
    ApplicationUser findByUsername(String username);
}