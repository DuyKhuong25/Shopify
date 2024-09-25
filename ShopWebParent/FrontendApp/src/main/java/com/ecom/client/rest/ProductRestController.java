package com.ecom.client.rest;

import com.ecom.client.service.ProductService;
import com.ecom.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductRestController {
    @Autowired
    private ProductService productService;

    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping("/products/filter_products")
    public Map<String, Object> filterProducts(@Param("categoryID") Integer categoryID,
                                              @Param("categories") Integer categories,
                                              @Param("minPrice") Integer minPrice,
                                              @Param("maxPrice") Integer maxPrice,
                                              @Param("searchQuery") String searchQuery){
        List<Product>  filteredProducts;
        if(categories == null && minPrice == null && maxPrice == null && (searchQuery == null || searchQuery.isEmpty())){
            filteredProducts = productService.listProductByCategoryId(categoryID);
        }else{
            filteredProducts = productService.filterProducts(categoryID, categories, minPrice, maxPrice, searchQuery);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("productsHTML", generateProductsHTML(filteredProducts));

        return response;
    }


    @GetMapping("/products/filter_search")
    public Map<String, Object> filterSearch(@Param("keyword") String keyword,
                                            @Param("minPrice") Integer minPrice,
                                            @Param("maxPrice") Integer maxPrice,
                                            @Param("searchQuery") String searchQuery){
        Map<String, Object> response = new HashMap<>();
        List<Product>  filteredProducts;

        if(keyword == null && minPrice == null && maxPrice == null && (searchQuery == null || searchQuery.isEmpty())){
            filteredProducts = productService.productSearch(keyword);
        }else{
            filteredProducts = productService.filterSearch(keyword, minPrice, maxPrice, searchQuery);
        }

        response.put("productsHTML", generateProductsHTML(filteredProducts));

        return response;
    }

    @GetMapping("/product/search/{keyword}")
    public Map<String, Object> searchLiveResult(@PathVariable String keyword){
        Map<String, Object> response = new HashMap<>();
        List<Product> productResult = productService.productSearch(keyword);

        String searchView = generateProductSearchHTML(productResult);
        response.put("searchView", searchView);
        return response;
    }

    private String generateProductSearchHTML(List<Product> productResult) {
        Context context = new Context();
        context.setVariable("productResult", productResult);
        return templateEngine.process("product/live-search", context);
    }

    private String generateProductsHTML(List<Product> products) {
        Context context = new Context();
        context.setVariable("products", products);
        return templateEngine.process("products", context);
    }

}
