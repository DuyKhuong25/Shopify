package com.ecom.admin.service;

import com.ecom.admin.repository.order.OrderTrackRepository;
import com.ecom.common.entity.OrderTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderTrackService {
    @Autowired
    private OrderTrackRepository orderTrackRepository;

    public List<OrderTrack> findOrderTrackByOrderId(int orderId) {
        return orderTrackRepository.findOrderTrackByOrderId(orderId);
    }
}
