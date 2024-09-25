package com.ecom.client.service;

import com.ecom.client.repository.ProductRepository;
import com.ecom.common.entity.Category;
import com.ecom.common.entity.Product;
import jdk.jfr.StackTrace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public void updateCountAndAverageRating(Integer productId){
        productRepository.updateCountAndAverageRating(productId);
    }

    public Product getProductById(int id) {
        return productRepository.findById(id);
    }

    public List<Product> listProductByCategoryId(int categoryId) {
        String categoryIdMatch = "-" + categoryId + "-";
        return productRepository.findProductByCategory(categoryId, categoryIdMatch);
    }

    public List<Product> filterProducts(Integer categoryID ,Integer categories, Integer minPrice, Integer maxPrice, String searchQuery) {
        return productRepository.findByFilters(categoryID, categories, minPrice, maxPrice, searchQuery);
    }

    public List<Product> filterSearch(String keyword, Integer minPrice, Integer maxPrice, String searchQuery) {
        return productRepository.findByFilterSearch(keyword, minPrice, maxPrice, searchQuery);
    }

    public Product getProductBySlug(String slug) {
        return productRepository.findProductByAlias(slug);
    }

    public List<Product> productSearch(String keyword) {
        return productRepository.productLiveSearch(keyword);
    }
}
