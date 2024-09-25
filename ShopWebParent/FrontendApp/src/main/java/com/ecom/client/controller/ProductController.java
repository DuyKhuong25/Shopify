package com.ecom.client.controller;

import com.ecom.client.MailConfig;
import com.ecom.client.service.*;
import com.ecom.common.entity.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProductController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    CustomerService customerService;

    @Autowired
    CartService cartService;

    @Autowired
    RatingService ratingService;

    @GetMapping("/san-pham/{category}")
    public String getProductByCategory(@PathVariable(name = "category") String category, Model model, HttpServletRequest request) {
        Integer quantityCart = getQuantityCart(request);

        Category categoryFind = categoryService.findCategoryByAlias(category);
        Integer categoryId = categoryFind.getId();

        List<Category> rootCategory = categoryService.findRootCategory();
        List<Category> subCategory = categoryService.getSubCategories(categoryFind);
        List<Product> products = productService.listProductByCategoryId(categoryId);

        model.addAttribute("quantityCart", quantityCart);
        model.addAttribute("rootCategory", rootCategory);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("subCategory", subCategory);
        model.addAttribute("products", products);

        return "product/product-list";
    }

    @GetMapping("/chi-tiet-san-pham/{slug}")
    public String getProductDetail(@PathVariable(name = "slug") String slug, Model model, HttpServletRequest request) {
        Integer quantityCart = getQuantityCart(request);

        Product product = productService.getProductBySlug(slug);
        List<Category> rootCategory = categoryService.findRootCategory();
        List<Rating> listRating = ratingService.getListRatingByProduct(product);

        model.addAttribute("quantityCart", quantityCart);
        model.addAttribute("rootCategory", rootCategory);
        model.addAttribute("product", product);
        model.addAttribute("listRating", listRating);

        return "product/product-detail";
    }

    @GetMapping("/tim-kiem")
    public String searchLiveResult(@Param("keyword") String keyword, Model model){
        List<Product> productSearch = productService.productSearch(keyword);
        List<Category> rootCategory = categoryService.findRootCategory();

        model.addAttribute("rootCategory", rootCategory);
        model.addAttribute("productSearch", productSearch);
        model.addAttribute("keyword", keyword);

        return "product/product-search";
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
