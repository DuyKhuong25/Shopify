package com.ecom.admin.service;

import com.ecom.admin.exception.ProductNotFoundException;
import com.ecom.admin.repository.product.ProductRepository;
import com.ecom.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ProductService {
    public static final int PRODUCT_PER_PAGE = 10;

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }

    public void updateCountAndAverageRating(Integer productId){
        productRepository.updateCountAndAverageRating(productId);
    }

    public Page<Product> findProductByPage(Integer pageNum, String keyword, Integer category) {
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_PER_PAGE);

        if(keyword != null && !keyword.isEmpty()) {
            if(!keyword.isEmpty() && category != null) {
                String categoryIdMatch = "-" + category + "-";
                return productRepository.productWithCategoryAndSearch(category,categoryIdMatch, keyword, pageable);
            }
            return productRepository.searchProduct(keyword, pageable);
        }

        if(category != null) {
            String categoryIdMatch = "-" + category + "-";
            return productRepository.productWithCategory(category, categoryIdMatch, pageable);
        }

        return productRepository.findAll(pageable);
    }

    public Product save(Product product) {
        if(product.getId() == null){
            product.setCreateTime(new Date());
        }

        if(product.getAlias() == null || product.getAlias().isEmpty()){
            product.setAlias(createSlug(product.getName()));
        }else{
            product.setAlias(createSlug(product.getAlias()));
        }

        product.setUpdateTime(new Date());

        return productRepository.save(product);
    }

    public String createSlug(String input){

        input = input.trim();

        input = input
                .replaceAll("[()]", "")
                .replaceAll("[-]+", " ")
                .replaceAll("/", "");

        String slug = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("đ", "d")
                .replaceAll("Đ", "d");

        slug = slug.toLowerCase()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "");

        return slug;
    }

    public void updateEnableProduct(Integer id, Boolean status){
        productRepository.updateProductEnabled(id, status);
    }

    public Product getProductById(Integer id) throws ProductNotFoundException {
        Product product = productRepository.getProductById(id);
        if(product == null){
            throw new ProductNotFoundException("Product not found with ID");
        }
        return product;
    }

    public void deleteProduct(Integer id) throws ProductNotFoundException{
        long countProduct = productRepository.countById(id);

        if(countProduct < 0 || countProduct == 0){
            throw new ProductNotFoundException("Couldn't foud product with ID");
        }else{
            productRepository.deleteById(id);
        }
    }

    public String checkUniqueProduct(Integer id, String name) {
        Product product = productRepository.findByName(name);
        Boolean isCreate = (id == null);

        if(product == null) return "OK";

        if(isCreate){
            if (product != null){ return "Duplicated";}
        }else{
           if(product.getId() != id){ return "Duplicated";}
        }

        return "OK";
    }
}
