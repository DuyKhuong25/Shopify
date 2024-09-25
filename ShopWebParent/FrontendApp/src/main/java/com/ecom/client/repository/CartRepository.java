package com.ecom.client.repository;

import com.ecom.common.entity.Cart;
import com.ecom.common.entity.Customer;
import com.ecom.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByCustomerId(int customerId);

    Cart findByCustomerAndProduct(Customer customer, Product product);

    @Query("UPDATE Cart c SET c.quantity = ?3 WHERE c.customer.id = ?1 AND c.product.id = ?2")
    @Modifying
    void updateCartQuantity(Integer customerId, Integer productId, Integer quantity);

    @Query("DELETE FROM Cart c WHERE c.customer.id = ?1 AND c.product.id = ?2")
    @Modifying
    void deleteByCustomerAndProduct(Integer customerId, Integer productId);

    @Query("DELETE FROM Cart c WHERE c.customer.id = ?1")
    @Modifying
    void deleteCartByCustomer(Integer customerId);
}
