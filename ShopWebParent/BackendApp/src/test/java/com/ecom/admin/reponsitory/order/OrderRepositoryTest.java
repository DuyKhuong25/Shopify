package com.ecom.admin.reponsitory.order;

import com.ecom.admin.repository.order.OrderRepository;
import com.ecom.common.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void createNewOneOrder(){
        Order order = new Order();
        
    }
}
