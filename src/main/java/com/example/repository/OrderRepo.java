package com.example.repository;


import com.example.model.Customer;
import com.example.model.Interface.BestSeller;
import com.example.model.Interface.Debter;
import com.example.model.Interface.SoldProductQuantity;
import com.example.model.Interface.TopCustomer;
import com.example.model.Order;
import com.example.model.OrderLine;
import com.example.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepo  extends JpaRepository<Order, Long> {

    Order save(Order order);
    List<Order> findAllByStaff(Staff staff);
    List<Order> findAllByCustomer(Customer customer);
    @Override
    List<Order> findAll();
    void deleteById(Long id);

    @Query("select o from Order o where o.createAt >= :date")
    List<Order> findAllByCreateAtAfter(@Param("date") LocalDate date);

    List<Order> findAllByCreateAtBefore(LocalDate date);
    List<Order> findAllByCreateAt(LocalDate date);
    Order getById(Long id);

    @Query("SELECT new com.example.model.OrderLine(ol.product.code, ol.quantity, ol.price) FROM OrderLine ol WHERE ol.order.id = :id")
    List<OrderLine> getOrderItemsById(@Param("id") Long id);

    //get all normal orders
    @Query("select o from Order o where o.isInstalment = false")
    List<Order> getAllNormalOrder();

    //get all instalment orders
    @Query("select o from Order o where o.isInstalment = true")
    List<Order> getALlInstalment();

    //get orders from startDate to endDate
    @Query("select o from Order o where o.createAt between :startDate and :endDate")
    List<Order> getAllByDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);



    @Query(nativeQuery = true, value = "SELECT c.name AS customerName, SUM(o.paid) AS totalPaid FROM order_ o," +
            " customer c where o.customer = c.id and o.create_at between :startDate and :endDate group by c.name order by totalPaid DESC")
    List<TopCustomer> getTopCustomer(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(nativeQuery = true, value = "SELECT o.product_code AS productCode, SUM(o.quantity) AS totalSold FROM order_line o" +
            " group by o.product_code order by totalSold DESC LIMIT 30" )
    List<BestSeller> getBestSeller();

    @Query(nativeQuery = true, value = "SELECT  * from order_ o where o.paid = 0")
    List<Order> getUnpaidOrder();

    @Query(nativeQuery = true, value = "delete from order_line o where o.order_id = :id")
    void deleteOrderLines(@Param("id") Long id);

    @Query(nativeQuery = true, value = "select sum(o.paid) from order_ o where create_at between :startDate and :endDate ")
    Float getIncomeByTime(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(nativeQuery = true, value = "select o.product_code as productCode, o.quantity as quantity, c.name as customer , o.order_id as orderId " +
            "from order_line o, order_ ord, customer c where c.id = ord.customer and o.product_code = :code and o.order_id = ord.id and ord.create_at between :startDate and :endDate")
    List<SoldProductQuantity> getSoldQuantity(@Param("code") String code, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(nativeQuery = true, value = "select c.name as customer, (select sum(ol.total_price) from order_line ol where ol.order_id = o.order_id) as total, ord.paid as paid, o.order_id as orderId, ord.create_at as date from order_line o, order_ ord, customer c where ord.customer = c.id and o.order_id = ord.id and ord.create_at between :startDate and :endDate group by o.order_id, c.name, ord.paid,ord.create_at")
    List<Debter> getDebter(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(nativeQuery = true, value = "select sum(o.paid) from order_ o where o.customer = :id")
    float getTotalPaidById(@Param("id") Long id);

    @Query(nativeQuery = true, value = "select sum(ol.total_price) from order_line ol, order_ o where ol.order_id = o.id and o.customer = :id")
    float getTotalToPayById(@Param("id") Long id);

    @Query(nativeQuery = true, value = "select * from order_ o where o.customer = :id")
    List<Order> getOrderByCustomer(@Param("id") Long id);
}
