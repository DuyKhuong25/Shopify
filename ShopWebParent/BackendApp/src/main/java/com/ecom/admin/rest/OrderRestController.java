package com.ecom.admin.rest;

import com.ecom.admin.service.OrderDetailService;
import com.ecom.admin.service.OrderService;
import com.ecom.common.entity.Order;
import com.ecom.common.entity.OrderDetail;
import com.ecom.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.NumberFormat;
import java.util.*;

@RestController
public class OrderRestController {
    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderService orderService;

    @Autowired
    TemplateEngine templateEngine;

    @GetMapping("/order/update/{orderDetailId}/{quantity}")
    public Map<String, Object> updateOrder(@PathVariable Integer orderDetailId, @PathVariable Integer quantity) {
        Map<String, Object> response = new HashMap<>();

        Map<String, Object> result = orderDetailService.updateOrderDetail(orderDetailId, quantity);
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

        response.put("subTotal", numberFormat.format(result.get("subTotal")));
        response.put("totalCheckout", numberFormat.format(result.get("totalCheckout")));

        return response;
    }

    @GetMapping("/order/update/add/{orderId}/{productId}")
    public Map<String, Object> updateAddProductOrder(@PathVariable Integer orderId, @PathVariable Integer productId) {
        Map<String, Object> response = new HashMap<>();
        Long totalCheckout = orderDetailService.updateOrderAddProduct(orderId, productId);
        Order order = orderService.findOrderById(orderId);

        String listOrderDetail = generateProductListHTML(order.getOrderDetails());

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

        response.put("totalCheckout", numberFormat.format(totalCheckout));
        response.put("listOrderDetail", listOrderDetail);

        return response;
    }


    private String generateProductListHTML(Set<OrderDetail> orderDetailList) {
        Context context = new Context();
        context.setVariable("orderDetailList", orderDetailList);
        return templateEngine.process("order/list-product", context);
    }

    @GetMapping("/order/update/search/{keyword}")
    public Map<String, Object> searchLiveResult(@PathVariable String keyword){
        Map<String, Object> response = new HashMap<>();
        List<Product> productResult = orderDetailService.productSearch(keyword);

        String searchView = generateProductSearchHTML(productResult);
        response.put("searchView", searchView);
        return response;
    }

    private String generateProductSearchHTML(List<Product> productResult) {
        Context context = new Context();
        context.setVariable("productResult", productResult);
        return templateEngine.process("order/search-view", context);
    }

}
