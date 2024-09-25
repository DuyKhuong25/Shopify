package com.ecom.admin.service;

import com.ecom.admin.repository.order.OrderRepository;
import com.ecom.admin.repository.order.OrderTrackRepository;
import com.ecom.common.entity.Order;
import com.ecom.common.entity.OrderStatus;
import com.ecom.common.entity.OrderTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order findOrder(Integer id) {
        return findOrderById(id);
    }

    public List<Order> findAllOrder() {
        return orderRepository.findAll();
    }

    public Order findOrderById(int id) {
        return orderRepository.getOrderById(id);
    }

    public void updateOrderStatus(Integer orderID, OrderStatus status) {
        Order order = orderRepository.getOrderById(orderID);
        OrderTrack orderTrack = new OrderTrack();
        orderTrack.setOrder(order);
        orderTrack.setTimeUpdated(new Date());
        orderTrack.setStatus(status);
        orderTrack.setNote(status.defaultDescription());
        order.addOrderTrack(orderTrack);

        orderRepository.updateOrderStatus(orderID, status);
    }
}
