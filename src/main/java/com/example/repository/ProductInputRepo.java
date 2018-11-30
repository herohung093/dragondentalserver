package com.example.repository;

import com.example.model.Inventory;
import com.example.model.ProductInput;
import com.example.model.Product;
import com.example.model.ProductInput;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository

public interface ProductInputRepo  extends JpaRepository<ProductInput, Long> {


    ProductInput save(ProductInput productInput);

    ProductInput getById(long id);

    List<ProductInput> findAll();

    @Query("select p from ProductInput p where p.product like %:size%")
    List<ProductInput> getAllBySize(@Param("size") String size);

    @Query("select p from ProductInput p where p.product like %:color%")
    List<ProductInput> getAllByColor(@Param("color") String color);

    @Query("select p from ProductInput p where p.createAt between :startDate and :endDate")
    List<ProductInput> getAllByDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("select p from ProductInput p where p.product = :code")
    List<ProductInput> getAllByCode(@Param("code") String code);

    void deleteById(Long id);

}
