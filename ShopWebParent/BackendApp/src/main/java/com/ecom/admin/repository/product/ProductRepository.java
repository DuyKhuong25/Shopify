package com.ecom.admin.repository.product;

import com.ecom.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findById(int id);

    Product findByName(String name);

    @Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
    @Modifying
    void updateProductEnabled(int id, boolean enabled);

    Long countById(@Param("id") Integer id);

    Product getProductById(@Param("id") Integer id);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
    Page<Product> searchProduct(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 OR p.category.allParentId LIKE %?2%")
    Page<Product> productWithCategory(Integer categoryId, String categoryIdMatch,Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.category.id = ?1 OR p.category.allParentId LIKE %?2%) AND (p.name LIKE %?3%)")
    Page<Product> productWithCategoryAndSearch(Integer categoryId, String categoryIdMatch, String keyword,Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
    List<Product> productLiveSearch(String keyword);

    @Query("UPDATE Product p SET p.averageRating = COALESCE((SELECT AVG(r.rating) FROM Rating r WHERE r.product.id = ?1), 0),"
            + "p.countRating = (SELECT COUNT(r.id) FROM Rating r WHERE r.product.id = ?1) WHERE p.id = ?1")
    @Modifying
    void updateCountAndAverageRating(Integer productId);
}
