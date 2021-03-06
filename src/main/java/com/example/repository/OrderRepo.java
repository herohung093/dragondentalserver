package com.example.repository;


import com.example.model.Customer;
import com.example.model.Interface.*;
import com.example.model.Order;
import com.example.model.OrderLine;
import com.example.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public interface OrderRepo  extends JpaRepository<Order, Long> {

    Order save(Order order);
    List<Order> findAllByStaff(Staff staff);
    List<Order> findAllByCustomer(Customer customer);

    @Query(nativeQuery = true, value = "select o.id as id, c.name as customer, o.create_at as createAt, o.update_at as updateAt, o.paid as paid, o.note as note, o.is_instalment as isInstalment, s.name as staff from order_ o , customer c, staff s where o.customer = c.id and o.staff = s.name")
    List<ResponseOrder> getAllOrder();
    void deleteById(Long id);

    @Query(nativeQuery = true, value = "select o.id as id, c.name as customer, o.create_at as createAt, o.update_at as updateAt, o.paid as paid, o.note as note, o.is_instalment as isInstalment, s.name as staff from order_ o , customer c, staff s where o.create_at >= :date and o.customer = c.id and o.staff = s.name")
    List<ResponseOrder> findAllByCreateAtAfter(@Param("date") LocalDate date);

    List<Order> findAllByCreateAtBefore(LocalDate date);
    List<Order> findAllByCreateAt(LocalDate date);

    @Query(nativeQuery = true, value = "select o.id as id, c.name as customer, o.create_at as createAt, o.update_at as updateAt, o.paid as paid, o.note as note, o.is_instalment as isInstalment, s.name as staff from order_ o , customer c, staff s where o.id = :id and o.customer = c.id and o.staff = s.name")
    ResponseOrder retrieveById(Long id);
    Order getById(Long id);

    @Query("SELECT new com.example.model.OrderLine(ol.product.code, ol.quantity, ol.price, ol.discount) FROM OrderLine ol WHERE ol.order.id = :id")
    List<OrderLine> getOrderItemsById(@Param("id") Long id);

    //get all normal orders
    @Query(nativeQuery = true, value = "select o.id as id, c.name as customer, o.create_at as createAt, o.update_at as updateAt, o.paid as paid, o.note as note, o.is_instalment as isInstalment, s.name as staff from order_ o , customer c, staff s where o.is_instalment = false and o.customer = c.id and o.staff = s.name")
    List<ResponseOrder> getAllNormalOrder();

    //get all instalment orders
    @Query(nativeQuery = true, value = "select o.id as id, c.name as customer, o.create_at as createAt, o.update_at as updateAt, o.paid as paid, o.note as note, o.is_instalment as isInstalment, s.name as staff from order_ o , customer c, staff s where o.is_instalment = true and o.customer = c.id and o.staff = s.name")
    List<ResponseOrder> getALlInstalment();

    //get orders from startDate to endDate
    @Query(nativeQuery = true, value = "select o.id as id, c.name as customer, o.create_at as createAt, o.update_at as updateAt, o.paid as paid, o.note as note, o.is_instalment as isInstalment, s.name as staff from order_ o , customer c, staff s where o.create_at between :startDate and :endDate and o.customer = c.id and o.staff = s.name")
    List<ResponseOrder> getAllByDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);



    @Query(nativeQuery = true, value = "SELECT c.name AS customerName, SUM(o.paid) AS totalPaid FROM order_ o," +
            " customer c where o.customer = c.id and o.create_at between :startDate and :endDate group by c.name order by totalPaid DESC")
    List<TopCustomer> getTopCustomer(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(nativeQuery = true, value = "SELECT o.product_code AS productCode, SUM(o.quantity) AS totalSold FROM order_line o, order_ ord where" +
            " o.order_id = ord.id and ord.create_at between :startDate and :endDate group by o.product_code order by totalSold DESC" )
    List<BestSeller> getBestSeller(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

   // @Query(nativeQuery = true, value = "SELECT  * from order_ o where o.paid = 0")
    @Query(nativeQuery = true, value = "select o.id as id, c.name as customer, o.create_at as createAt, o.update_at as updateAt, o.paid as paid, o.note as note, o.is_instalment as isInstalment, s.name as staff from order_ o , customer c, staff s where o.paid = 0 and o.customer = c.id and o.staff = s.name")
    List<ResponseOrder> getUnpaidOrder();
    @Modifying
    @Query(nativeQuery = true, value = "delete from order_line o where o.order_id = :id")
    void deleteOrderLines(@Param("id") Long id);

    @Query(nativeQuery = true, value = "select sum(o.paid) from order_ o where create_at between :startDate and :endDate ")
    Float getIncomeByTime(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(nativeQuery = true, value = "select o.product_code as productCode, o.quantity as quantity, c.name as customer , o.order_id as orderId " +
            "from order_line o, order_ ord, customer c where c.id = ord.customer and o.product_code = :code and o.order_id = ord.id and ord.create_at between :startDate and :endDate")
    List<SoldProductQuantity> getSoldQuantity(@Param("code") String code, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(nativeQuery = true, value = "select c.name as customer, (select sum(ol.total_price) from order_line ol where ol.order_id = o.order_id) as total, ord.paid as paid, o.order_id as orderId, " +
            "ord.create_at as date from order_line o, order_ ord, customer c where ord.customer = c.id and o.order_id = ord.id and ord.create_at between :startDate and :endDate group by o.order_id, c.name, ord.paid,ord.create_at")
    List<Debter> getDebter(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(nativeQuery = true, value = "select sum(o.paid) from order_ o where o.customer = :id")
    float getTotalPaidById(@Param("id") Long id);

    @Query(nativeQuery = true, value = "select sum(ol.total_price) from order_line ol, order_ o where ol.order_id = o.id and o.customer = :id")
    float getTotalToPayById(@Param("id") Long id);

    @Query(nativeQuery = true, value = "select o.id as id, c.name as customer, o.create_at as createAt, o.update_at as updateAt, o.paid as paid, o.note as note, o.is_instalment as isInstalment, s.name as staff from order_ o , customer c, staff s where o.customer = :id and o.customer = c.id and o.staff = s.name")
    List<ResponseOrder> getOrderByCustomer(@Param("id") Long id);

    @Query(nativeQuery = true, value = "select ol.product_code as product, sum(ol.quantity) as quantity from order_line ol, order_ ord where ord.id = ol.order_id and ord.customer = :id and ord.create_at between :startDate and :endDate group by ol.product_code")
    List<CustomerSold> getCustomerSold(@Param("id") Long id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Order o set o.paid = :paid, o.note = :note, o.isInstalment = :instalment where o.id = :id")
    void updateOrder(@Param("id") long id,@Param("paid") float paid, @Param("note") String note,@Param("instalment")boolean instalment);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(nativeQuery = true, value = "insert into order_line (order_id, product_code, price, quantity, discount, total_price, id) values (:order, :code,:price,:quantity,:discount,:totalPrice,:id)")
    void updateOrderItem(@Param("order")long order,@Param("code")String code,@Param("price")float price,
                         @Param("quantity")int quantity, @Param("discount")int discount, @Param("totalPrice")float totalPrice, @Param("id")int id);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Order o set o.paid = :amount where o.id = :id")
    void payForOrder(@Param("id")long id, @Param("amount")float amount);
}
