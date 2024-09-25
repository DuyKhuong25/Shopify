package com.ecom.client.controller;

import com.ecom.client.MailConfig;
import com.ecom.client.exception.CustomerNotFoundException;
import com.ecom.client.service.CartService;
import com.ecom.client.service.CategoryService;
import com.ecom.client.service.CustomerService;
import com.ecom.client.service.ProductService;
import com.ecom.common.entity.Cart;
import com.ecom.common.entity.Category;
import com.ecom.common.entity.Customer;
import com.ecom.common.entity.Product;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ClientController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    CustomerService customerService;

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {
        Integer quantityCart = getQuantityCart(request);

        List<Category> rootCategory = categoryService.findRootCategory();

        Map<String, List<Product>> data = new HashMap<>();
        for(Category category : rootCategory) {
            List<Product> products = productService.listProductByCategoryId(category.getId());
            data.put(category.getName(), products);
        }

        model.addAttribute("data", data);
        model.addAttribute("quantityCart", quantityCart);
        model.addAttribute("rootCategory", rootCategory);

        return "home/home";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("customer", new Customer());
        return "form/register";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "form/login";
    }

    @GetMapping("/cart")
    public String getCart(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Long totalCartPrice = 0L;
        Integer quantityCart = getQuantityCart(request);
        Customer customer = getAuthenticatedCustomer(request);

        List<Category> rootCategory = categoryService.findRootCategory();
        List<Cart> listCart = cartService.getCartByCustomer(customer.getId());

        for(Cart cartItem : listCart){
            totalCartPrice += (cartItem.getProduct().getDiscountPrice() * cartItem.getQuantity());
        }

        model.addAttribute("listCart", listCart);
        model.addAttribute("quantityCart", quantityCart);
        model.addAttribute("rootCategory", rootCategory);
        model.addAttribute("totalCartPrice", totalCartPrice);

        return "cart/cart-list";
    }

    public Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = MailConfig.getEmailOfAuthenticatedCustomer(request);
        if (email == null) {
            throw new CustomerNotFoundException("Customer not found!");
        }
        return customerService.getCustomerByEmail(email);
    }

    public Integer getQuantityCart(HttpServletRequest request) {
        Integer quantityCart = 0;

        String email = MailConfig.getEmailOfAuthenticatedCustomer(request);
        Customer customer = customerService.getCustomerByEmail(email);
        if(customer == null){
            quantityCart = 0;
        }else{
            List<Cart> cartOfCustomer = cartService.getCartByCustomer(customer.getId());
            for(Cart cart : cartOfCustomer){
                quantityCart += cart.getQuantity();
            }
        }

        return quantityCart;
    }
}
