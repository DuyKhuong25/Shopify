package com.ecom.admin.reponsitory.order;

import com.ecom.admin.repository.order.OrderTrackRepository;
import com.ecom.common.entity.Order;
import com.ecom.common.entity.OrderStatus;
import com.ecom.common.entity.OrderTrack;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class OrderTrackRepositoryTest {
    @Autowired
    private OrderTrackRepository orderTrackRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void testUpdateOrderTrack() {
        OrderTrack orderTrack = new OrderTrack();
        Order order = entityManager.find(Order.class, 1);
        List<OrderTrack> orderTracks = order.getOrderTracks();

        orderTrack.setOrder(order);
        orderTrack.setStatus(OrderStatus.SHIPPING);
        orderTrack.setNote(OrderStatus.SHIPPING.defaultDescription());
        orderTrack.setTimeUpdated(new Date());

        order.addOrderTrack(orderTrack);

        System.out.println(orderTrack);
    }

    @Test
    public void testFindOrderTrackByOrderId() {
        Integer orderId = 1;
        List<OrderTrack> orderTracks = orderTrackRepository.findOrderTrackByOrderId(orderId);
        System.out.println(orderTracks);
    }
}
