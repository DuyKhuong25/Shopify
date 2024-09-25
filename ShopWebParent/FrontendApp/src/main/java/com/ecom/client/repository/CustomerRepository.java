package com.ecom.client.repository;

import com.ecom.common.entity.AuthenticationType;
import com.ecom.common.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE c.verificationCode = ?1")
    Customer findByTokenVerify(String token);

    @Query("UPDATE Customer c SET c.enabled = true, c.verificationCode = null WHERE c.id = ?1")
    @Modifying
    void updateCustomerActive(int id);

    @Query("UPDATE Customer c SET c.authenticationType = ?2 WHERE c.id = ?1")
    @Modifying
    void updateCustomerAuthenticationType(int id, AuthenticationType authenticationType);
}
