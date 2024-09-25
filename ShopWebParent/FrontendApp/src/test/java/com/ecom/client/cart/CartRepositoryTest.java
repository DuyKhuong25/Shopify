package com.ecom.client.cart;

import com.ecom.client.repository.AddressRepository;
import com.ecom.client.repository.CartRepository;
import com.ecom.common.entity.Address;
import com.ecom.common.entity.Cart;
import com.ecom.common.entity.Customer;
import com.ecom.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CartRepositoryTest {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    AddressRepository addressRepository;

    @Test
    public void createCart(){
        Cart cart1 = new Cart();
        cart1.setCustomer(new Customer(4));
        cart1.setProduct(new Product(25));
        cart1.setQuantity(1);

        cartRepository.saveAll(List.of(cart1));
    }

    @Test
    public void findByCustomerAndProduct(){
        Cart cart = cartRepository.findByCustomerAndProduct(new Customer(3), new Product(22));
        System.out.println(cart);
    }

    @Test
    public void updateQuantity(){
        cartRepository.updateCartQuantity(4, 25, 2);
    }

    @Test
    public void deleteByCustomerAndProduct(){
        cartRepository.deleteByCustomerAndProduct(3, 25);
    }

    @Test
    public void getDefaultAddress(){
        int customerId = 3;
        Address address = addressRepository.getAddressDefault(customerId);
        System.out.println(address);
    }
}
