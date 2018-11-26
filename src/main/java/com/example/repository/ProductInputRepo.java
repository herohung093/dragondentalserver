package com.example.repository;

import com.example.model.ProductInput;
import com.example.model.Product;
import com.example.model.ProductInput;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository

public interface ProductInputRepo  extends JpaRepository<ProductInput, Long> {


    ProductInput save(ProductInput productInput);

    List<ProductInput> findAllById(long id);

    List<ProductInput> findAll();
}
