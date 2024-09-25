package com.ecom.admin.controller;

import com.ecom.admin.service.OrderDetailService;
import com.ecom.admin.service.OrderService;
import com.ecom.admin.service.OrderTrackService;
import com.ecom.common.entity.Order;
import com.ecom.common.entity.OrderStatus;
import com.ecom.common.entity.OrderTrack;
import com.ecom.common.entity.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderTrackService orderTrackService;

    @GetMapping("/orders")
    public String getOrderList(Model model){
        List<Order> orders = orderService.findAllOrder();
        Map<OrderStatus, String> orderStatus = translateOrderStatus();
        Map<PaymentMethod, String> typeCheckout = translatePaymentMethod();

        model.addAttribute("orders", orders);
        model.addAttribute("orderStatus", orderStatus);
        model.addAttribute("typeCheckout", typeCheckout);
        model.addAttribute("title", "Danh sách đơn hàng");
        return "/order/order-list";
    }


    @GetMapping("/orders/detail/{orderID}")
    public String getOrderDetail(@PathVariable("orderID") Integer orderID, Model model){
        Order order = orderService.findOrderById(orderID);
        Map<OrderStatus, String> orderStatus = translateOrderStatus();
        Map<PaymentMethod, String> typeCheckout = translatePaymentMethod();
        List<OrderTrack> listOrderTrack = orderTrackService.findOrderTrackByOrderId(orderID);

        model.addAttribute("order", order);
        model.addAttribute("orderStatus", orderStatus);
        model.addAttribute("typeCheckout", typeCheckout);
        model.addAttribute("listOrderTrack", listOrderTrack);
        model.addAttribute("listOrderStatus", OrderStatus.values());
        model.addAttribute("title", "Thông tin đơn đặt hàng");

        return "/order/order-detail";
    }

    @GetMapping("/orders/update/{orderID}")
    public String updateOrderDetail(@PathVariable("orderID") Integer orderID, Model model){
        Order order = orderService.findOrderById(orderID);
        Map<PaymentMethod, String> typeCheckout = translatePaymentMethod();

        model.addAttribute("order", order);
        model.addAttribute("typeCheckout", typeCheckout);
        model.addAttribute("title", "Chỉnh sửa đơn hàng");

        return "/order/edit-order";
    }


    @GetMapping("/orders/status/update/{orderID}/{status}")
    public String updateOrderStatus(@PathVariable("orderID") Integer orderID, @PathVariable("status") OrderStatus status){
        orderService.updateOrderStatus(orderID, status);
        return "redirect:/orders/detail/" + orderID;
    }

    @GetMapping("/order/detail/delete/{orderId}/{orderDetailId}")
    public String deleteOrderDetail(@PathVariable Integer orderId, @PathVariable Integer orderDetailId){
        orderDetailService.deleteOrderDetailItem(orderId, orderDetailId);
        return "redirect:/orders/update/" + orderId;
    }

    public Map<PaymentMethod, String> translatePaymentMethod(){
        Map<PaymentMethod, String> typeCheckout = new HashMap<>();

        typeCheckout.put(PaymentMethod.COD, "Thanh toán khi nhận hàng");
        typeCheckout.put(PaymentMethod.CREDIT_CART, "Chuyển khoản ngân hàng");

        return typeCheckout;
    }

    public  Map<OrderStatus, String> translateOrderStatus(){
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
