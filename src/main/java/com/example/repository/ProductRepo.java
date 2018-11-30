package com.example.repository;



import com.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public interface ProductRepo extends JpaRepository<Product, String> {

    @Query(value = "select p from Product p where p.code = :code")
    Product findByCode(@Param("code")String code);
    @Override
    List<Product> findAll();

    Product save(Product p);

    void deleteByCode(String code);

    //get all product with the color
    @Query("select p from Product p where p.code like %:color%")
    List<Product> getAllByColor(@Param("color") String color);

    @Query("select p from Product p where p.code like %:size%")
    List<Product> getAllBySize(@Param("size") String size);
}
