package com.ecom.client.service;

import com.ecom.client.repository.OrderRepository;
import com.ecom.common.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getListOrderByCustomer(Customer customer){
        return orderRepository.findByCustomer(customer);
    }

    public Order getOrderById(Integer id){
        return orderRepository.findOrderById(id);
    }

    public Order createOrder(Customer customer, Address address, List<Cart> carts, PaymentMethod paymentMethod, CheckoutInfo checkoutInfo) {
        Order order = new Order();
        order.setCustomer(customer);
        order.copyAddressCustomer(address);
        order.setOrderTime(new Date());
        order.setDeliverDate(checkoutInfo.getDeliverDate());
        order.setTotal(checkoutInfo.getPaymentTotal());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(paymentMethod);

        Set<OrderDetail> orderDetailList = order.getOrderDetails();
        for (Cart cartItem : carts){
            Product product = cartItem.getProduct();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setSubtotal(cartItem.getSubTotalCartItem());
            orderDetail.setUnitPrice(cartItem.getProduct().getDiscountPrice());

            orderDetailList.add(orderDetail);
        }

        return orderRepository.save(order);
    }
}
