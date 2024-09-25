package com.ecom.client.repository;

import com.ecom.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findById(int id);

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 OR p.category.allParentId LIKE %?2 AND p.enabled = true")
    List<Product> findProductByCategory(Integer id, String categoryMatch);

    @Query("SELECT p FROM Product p WHERE " +
            "p.enabled = true AND " +
            "(p.category.id = :categoryID OR p.category.allParentId LIKE CONCAT('%-', :categoryID, '-%')) AND " +
            "(:categories IS NULL OR p.category.id = :categories) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:searchQuery IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')))")
    List<Product> findByFilters(@Param("categoryID") Integer categoryID,
                                @Param("categories") Integer categories,
                                @Param("minPrice") Integer minPrice,
                                @Param("maxPrice") Integer maxPrice,
                                @Param("searchQuery") String searchQuery);

    @Query("SELECT p FROM Product p WHERE " +
            "p.enabled = true AND " +
            "(p.name LIKE %:keyword%) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:searchQuery IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')))")
    List<Product> findByFilterSearch(@Param("keyword") String keyword,
                                @Param("minPrice") Integer minPrice,
                                @Param("maxPrice") Integer maxPrice,
                                @Param("searchQuery") String searchQuery);

    Product findProductByAlias(String alias);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
    List<Product> productLiveSearch(String keyword);

    @Query("UPDATE Product p SET p.averageRating = COALESCE((SELECT AVG(r.rating) FROM Rating r WHERE r.product.id = ?1), 0),"
            + "p.countRating = (SELECT COUNT(r.id) FROM Rating r WHERE r.product.id = ?1) WHERE p.id = ?1")
    @Modifying
    void updateCountAndAverageRating(Integer productId);
}
