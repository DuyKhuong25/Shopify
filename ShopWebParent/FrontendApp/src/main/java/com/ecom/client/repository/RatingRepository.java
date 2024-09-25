package com.ecom.client.repository;

import com.ecom.common.entity.Product;
import com.ecom.common.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    @Query("SELECT COUNT(r.id) FROM Rating r WHERE r.customer.id = ?1 AND r.product.id = ?2")
    Long countByCustomerIdAndProductId(Integer customerId, Integer productId);

    List<Rating> findByProduct(Product product);

    Rating findByCustomerIdAndProductId(Integer customerId, Integer productId);
}
