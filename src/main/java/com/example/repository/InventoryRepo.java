package com.example.repository;

import com.example.model.Inventory;
import com.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface InventoryRepo  extends JpaRepository<Inventory, String> {
    Inventory findByCode(String code);

    @Override
    List<Inventory> findAll();

    @Query("select i from Inventory i where i.code like %:color%")
    List<Inventory> getAllByColor(@Param("color") String color);

    @Query("select i from Inventory i where i.code like %:size%")
    List<Inventory> getAllBySize(@Param("size") String size);

    @Modifying
    @Query("update Inventory i Set i.stock= i.stock - :quantity WHERE i.code = :code")
    void decreaseQuantity(@Param("code") String code, @Param("quantity") int quantity);

    @Modifying
    @Query("update Inventory i Set i.stock= i.stock + :quantity WHERE i.code = :code")
    void increaseQuantity(@Param("code") String code,@Param("quantity") int quantity);

    @Modifying
    @Query("update Inventory i Set i.stock= :quantity WHERE i.code = :code")
    void updateStock(@Param("code") String code, @Param("quantity") int quantity);

    Inventory save(Inventory inventory);
}
