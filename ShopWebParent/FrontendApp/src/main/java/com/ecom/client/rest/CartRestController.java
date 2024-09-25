package com.ecom.client.rest;

import com.ecom.client.MailConfig;
import com.ecom.client.exception.CustomerNotFoundException;
import com.ecom.client.service.CartService;
import com.ecom.client.service.CustomerService;
import com.ecom.common.entity.Cart;
import com.ecom.common.entity.Customer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
public class CartRestController {
    @Autowired
    CustomerService customerService;

    @Autowired
    CartService cartService;

    @GetMapping("/cart/add/{productID}/{quantity}")
    public Map<String, Object> addProductToCart(@PathVariable int productID, @PathVariable int quantity, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Customer customer = getAuthenticatedCustomer(request);
            cartService.addCart(quantity, customer, productID);
            List<Cart> cartOfCustomer = cartService.getCartByCustomer(customer.getId());
            Integer quantityCart = 0;
            for (Cart cart : cartOfCustomer) {
                quantityCart += cart.getQuantity();
            }

            response.put("responseType", "Success");
            response.put("totalQuantity", quantityCart);
            return response;

        } catch (CustomerNotFoundException ex) {
            response.put("responseType", "Authentication");
            return response;
        }
    }

    @GetMapping("/cart/update/{productID}/{quantity}")
    public Map<String, Object> updateProductCart(@PathVariable int productID, @PathVariable int quantity, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        String email = MailConfig.getEmailOfAuthenticatedCustomer(request);
        Customer customer = customerService.getCustomerByEmail(email);

        long subTotalCartItem = cartService.updateCartItem(customer.getId(), productID, quantity);
        List<Cart> cartOfCustomer = cartService.getCartByCustomer(customer.getId());

        Long totalCartPrice = 0L;
        Integer quantityCart = 0;

        for (Cart cart : cartOfCustomer) {
            quantityCart += cart.getQuantity();
            totalCartPrice += cart.getProduct().getDiscountPrice() * cart.getQuantity();
        }

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

        response.put("quantityCart", quantityCart);
        response.put("subTotalPrice", numberFormat.format(subTotalCartItem));
        response.put("cartTotalPrice", numberFormat.format(totalCartPrice));

        return response;
    }

    @GetMapping("/cart/delete/{productID}")
    public Map<String, Object> deleteProductCart(@PathVariable int productID, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        String email = MailConfig.getEmailOfAuthenticatedCustomer(request);
        Customer customer = customerService.getCustomerByEmail(email);

        cartService.deleteCart(customer.getId(), productID);

        List<Cart> cartOfCustomer = cartService.getCartByCustomer(customer.getId());

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

        Integer quantityCart = 0;
        Long totalCartPrice = 0L;

        for (Cart cart : cartOfCustomer) {
            quantityCart += cart.getQuantity();
            totalCartPrice += cart.getProduct().getDiscountPrice() * cart.getQuantity();
        }

        response.put("quantityCart", quantityCart);
        response.put("cartTotalPrice", numberFormat.format(totalCartPrice));

        return response;
    }

    public Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = MailConfig.getEmailOfAuthenticatedCustomer(request);
        if (email == null) {
            throw new CustomerNotFoundException("Customer not found!");
        }
        return customerService.getCustomerByEmail(email);
    }
}
