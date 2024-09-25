package com.ecom.admin.repository.order;

import com.ecom.common.entity.Order;
import com.ecom.common.entity.OrderDetail;
import com.ecom.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    OrderDetail findOrderDetailById(int id);

    OrderDetail findOrderDetailByOrderAndProduct(Order order, Product product);

    @Query("DELETE FROM OrderDetail o WHERE o.id = ?2 AND o.order.id = ?1")
    @Modifying
    void deleteOrderDetail(int orderId, int orderDetailId);
}
