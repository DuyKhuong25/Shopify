package com.ecom.client.repository;

import com.ecom.common.entity.Address;
import com.ecom.common.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findByCustomer(Customer customer);

    Address findByIdAndCustomer(Integer id, Customer customer);

    @Query("SELECT a FROM Address a WHERE a.customer.id = ?1 AND a.addressDefault = true")
    Address getAddressDefault(Integer customerId);

    @Query("UPDATE Address a SET a.addressDefault = true WHERE a.id = ?1 AND a.customer.id = ?2")
    @Modifying
    void updateDefaultAddress(Integer addressId, Integer customerId);

    @Query("UPDATE Address a SET a.addressDefault = false WHERE a.id != ?1 AND a.customer.id = ?2")
    @Modifying
    void updateDefaultOtherAddress(Integer addressId, Integer customerId);

    @Query("DELETE FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
    @Modifying
    void deleteAddress(Integer addressId, Integer customerId);
}
