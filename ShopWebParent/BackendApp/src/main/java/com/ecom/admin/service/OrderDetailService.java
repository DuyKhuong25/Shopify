package com.ecom.admin.service;

import com.ecom.admin.repository.order.OrderDetailRepository;
import com.ecom.admin.repository.order.OrderRepository;
import com.ecom.admin.repository.product.ProductRepository;
import com.ecom.common.entity.Order;
import com.ecom.common.entity.OrderDetail;
import com.ecom.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public Map<String, Object> updateOrderDetail(Integer orderDetailId, Integer quantity) {
        Map<String, Object> result = new HashMap<>();

        OrderDetail orderDetail = orderDetailRepository.findOrderDetailById(orderDetailId);
        Order order = orderDetail.getOrder();

        long totalCheckout = 0L;
        long subTotal = orderDetail.getUnitPrice() * quantity;
        orderDetail.setSubtotal(subTotal);

        Set<OrderDetail> listOrderDetail = order.getOrderDetails();
        for(OrderDetail orderDetailItem : listOrderDetail) {
            totalCheckout += orderDetailItem.getSubtotal();
        }

        orderDetail.setSubtotal(subTotal);
        orderDetail.setQuantity(quantity);
        order.setTotal(totalCheckout);

        orderRepository.save(order);

        result.put("subTotal", subTotal);
        result.put("totalCheckout", totalCheckout);

        return result;
    }

    public Long updateOrderAddProduct(Integer orderId, Integer productId){
        Order order = orderRepository.getOrderById(orderId);
        Product product = productRepository.getProductById(productId);
        OrderDetail orderDetail = orderDetailRepository.findOrderDetailByOrderAndProduct(order, product);
        long totalCheckout = 0L;

        if(orderDetail != null){
            orderDetail.setQuantity(orderDetail.getQuantity() + 1);
            orderDetail.setSubtotal(orderDetail.getQuantity() * orderDetail.getUnitPrice());
        }else{
            orderDetail = new OrderDetail();
            orderDetail.setQuantity(1);
            orderDetail.setSubtotal(product.getDiscountPrice());
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setUnitPrice(product.getDiscountPrice());
            order.getOrderDetails().add(orderDetail);
        }


        Set<OrderDetail> listOrderDetail = order.getOrderDetails();
        for(OrderDetail orderDetailItem : listOrderDetail) {
            totalCheckout += orderDetailItem.getSubtotal();
        }
        order.setTotal(totalCheckout);

        orderRepository.save(order);

        return totalCheckout;
    }

    public void deleteOrderDetailItem(Integer orderId, Integer orderDetailId){
        orderDetailRepository.deleteOrderDetail(orderId, orderDetailId);
        long totalCheckout = 0L;
        Order order = orderRepository.getOrderById(orderId);
        Set<OrderDetail> listOrderDetail = order.getOrderDetails();
        for(OrderDetail orderDetailItem : listOrderDetail) {
            totalCheckout += orderDetailItem.getSubtotal();
        }
        order.setTotal(totalCheckout);

        orderRepository.save(order);
    }

    public List<Product> productSearch(String keyword) {
        return productRepository.productLiveSearch(keyword);
    }
}
