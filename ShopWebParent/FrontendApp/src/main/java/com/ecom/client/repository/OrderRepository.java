package com.ecom.client.repository;

import com.ecom.common.entity.Customer;
import com.ecom.common.entity.Order;
import com.ecom.common.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o WHERE o.customer = ?1 ORDER BY o.orderTime DESC")
    List<Order> findByCustomer(Customer customer);

    Order findOrderById(int id);
}
