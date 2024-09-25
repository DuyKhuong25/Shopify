package com.ecom.client.controller;

import com.ecom.client.MailConfig;
import com.ecom.client.exception.CustomerNotFoundException;
import com.ecom.client.service.CustomerService;
import com.ecom.client.service.ProductService;
import com.ecom.client.service.RatingService;
import com.ecom.common.entity.Customer;
import com.ecom.common.entity.Product;
import com.ecom.common.entity.Rating;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class RatingController {
    @Autowired
    ProductService productService;

    @Autowired
    RatingService ratingService;

    @Autowired
    CustomerService customerService;

    @GetMapping("/rating/write/{productAlias}")
    public String getWriteRating(@PathVariable(name = "productAlias") String productAlias,
                                 @RequestParam("order") Integer orderID,
                                 Model model){
        Product product = productService.getProductBySlug(productAlias);
        Rating ratingObject = new Rating();

        model.addAttribute("orderID", orderID);
        model.addAttribute("ratingObject", ratingObject);
        model.addAttribute("product", product);
        return "order/rating-form";
    }

    @GetMapping("/rating/read/{productAlias}")
    public String getReadRating(@PathVariable(name = "productAlias") String productAlias, Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Product product = productService.getProductBySlug(productAlias);
        Customer customer = getAuthenticatedCustomer(request);
        Rating ratingObject = ratingService.getRatingByCustomerAndProduct(customer, product);

        model.addAttribute("product", product);
        model.addAttribute("ratingObject", ratingObject);
        return "order/rating-form";
    }

    @PostMapping("/rating/save")
    public String saveRating(@ModelAttribute(name = "ratingObject") Rating rating,
                             @Param("productID") Integer productID,
                             @RequestParam("orderID") Integer orderID,
                             HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        Product product = productService.getProductById(productID);
        rating.setCustomer(customer);
        rating.setProduct(product);
        rating.setCreateTime(new Date());

        ratingService.saveRating(rating);
        productService.updateCountAndAverageRating(productID);

        return "redirect:/orders/" + orderID;
    }

    public Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = MailConfig.getEmailOfAuthenticatedCustomer(request);
        if (email == null) {
            throw new CustomerNotFoundException("Customer not found!");
        }
        return customerService.getCustomerByEmail(email);
    }
}
