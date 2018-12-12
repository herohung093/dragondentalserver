package com.example.repository;

import com.example.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    Customer save(Customer customer);
    Customer findByName(String name);
    Customer findById (long id);
    @Override
    List<Customer> findAll();

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Customer o set o.name =:name, o.address =:address, o.contactPerson =:contactPerson, o.note = :note, o.phone =:phone where o.id = :id")
    void updateCustomer(@Param("id") long id, @Param("name") String name,@Param("phone") String phone, @Param("address")  String address
            , @Param("contactPerson")  String contactPerson, @Param("note") String note);
}
