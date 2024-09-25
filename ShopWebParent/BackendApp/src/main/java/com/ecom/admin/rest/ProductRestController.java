package com.ecom.admin.rest;

import com.ecom.admin.repository.product.ProductRepository;
import com.ecom.admin.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {
    @Autowired
    private ProductService productService;

    @PostMapping("/products/check_unique")
    public String checkUniqueProduct(@Param("id") Integer id, @Param("name") String name) {
        return productService.checkUniqueProduct(id, name);
    }
}
