package com.example.repository;

import com.example.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepo extends JpaRepository<Staff, String> {

    Staff save(Staff s);
    Staff findByName(String Name);
    List<Staff> findAll();
}
