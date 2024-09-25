package com.ecom.client.controller;

import com.ecom.client.MailConfig;
import com.ecom.client.exception.CustomerNotFoundException;
import com.ecom.client.service.CategoryService;
import com.ecom.client.service.CustomerService;
import com.ecom.client.service.OrderService;
import com.ecom.client.service.RatingService;
import com.ecom.common.entity.Customer;
import com.ecom.common.entity.Order;
import com.ecom.common.entity.OrderDetail;
import com.ecom.common.entity.OrderStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    RatingService ratingService;

    @GetMapping("/orders")
    public String getListOrderCustomer(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        List<Order> listOrder = orderService.getListOrderByCustomer(customer);

        model.addAttribute("listOrder", listOrder);
        model.addAttribute("statusOrder", translateOrderStatus());
        model.addAttribute("rootCategory", categoryService.findRootCategory());

        return "order/order-list";
    }

    @GetMapping("/orders/{id}")
    public String getDetailOrderCustomer(@PathVariable Integer id, Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Order order = orderService.getOrderById(id);
        Set<OrderDetail> listOrderDetail = order.getOrderDetails();
        checkCustomerRatedProduct(listOrderDetail, request);
        OrderStatus orderSuccess = OrderStatus.SUCCESS;

        model.addAttribute("order", order);
        model.addAttribute("orderSuccess", orderSuccess);
        model.addAttribute("statusOrder", translateOrderStatus());
        model.addAttribute("rootCategory", categoryService.findRootCategory());

        return "order/rating-list";
    }

    public void checkCustomerRatedProduct(Set<OrderDetail> listOrderDetail, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        for (OrderDetail orderDetail : listOrderDetail) {
            boolean isRated = ratingService.ratedByCustomerAndProduct(customer.getId(), orderDetail.getProduct().getId());
            if (isRated) {
                orderDetail.getProduct().setRated(true);
            }else{
                orderDetail.getProduct().setRated(false);
            }
        }
    }

    public Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = MailConfig.getEmailOfAuthenticatedCustomer(request);
        if (email == null) {
            throw new CustomerNotFoundException("Customer not found!");
        }
        return customerService.getCustomerByEmail(email);
    }

    public Map<OrderStatus, String> translateOrderStatus(){
        Map<OrderStatus, String> orderStatus = new HashMap<>();

        orderStatus.put(OrderStatus.PENDING, "Chờ xác nhận");
        orderStatus.put(OrderStatus.PROCESSING, "Đang xử lý");
        orderStatus.put(OrderStatus.SHIPPING, "Đang vận chuyển");
        orderStatus.put(OrderStatus.CANCEL, "Hủy đơn hàng");
        orderStatus.put(OrderStatus.RETURNED, "Hoàn trả hàng");
        orderStatus.put(OrderStatus.SUCCESS, "Giao thành công");

        return orderStatus;
    }
}
