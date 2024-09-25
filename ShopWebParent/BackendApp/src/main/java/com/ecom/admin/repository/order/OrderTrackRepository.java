package com.ecom.admin.repository.order;

import com.ecom.common.entity.Order;
import com.ecom.common.entity.OrderTrack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTrackRepository extends JpaRepository<OrderTrack, Integer> {
    List<OrderTrack> findOrderTrackByOrderId(Integer orderId);
}
