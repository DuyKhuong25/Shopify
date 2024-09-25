package com.ecom.admin.repository.order;

import com.ecom.common.entity.Order;
import com.ecom.common.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order getOrderById(int id);

    @Query("UPDATE Order o SET o.status = ?2 WHERE o.id = ?1")
    @Modifying
    void updateOrderStatus(Integer id, OrderStatus status);
}
