package com.ecom.client.service;

import com.ecom.client.repository.CartRepository;
import com.ecom.common.entity.Cart;
import com.ecom.common.entity.Customer;
import com.ecom.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public void addCart(Integer quantity, Customer customer, Integer productID) {
        Integer newQuantity = quantity;
        Product product = new Product(productID);
        Cart cart = cartRepository.findByCustomerAndProduct(customer, product);

        if(cart != null){
            newQuantity = cart.getQuantity() + quantity;
        }else{
            cart = new Cart();
            cart.setCustomer(customer);
            cart.setProduct(product);
        }

        cart.setQuantity(newQuantity);

        cartRepository.save(cart);
    }

    public List<Cart> getCartByCustomer(Integer customerID) {
        return cartRepository.findByCustomerId(customerID);
    }

    public Long updateCartItem(Integer customerId, Integer productId, Integer quantity){
        cartRepository.updateCartQuantity(customerId, productId, quantity);
        Cart cartItem = cartRepository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
        long subTotalCatItem = cartItem.getProduct().getDiscountPrice() * cartItem.getQuantity();
        return subTotalCatItem;
    }

    public void deleteCart(Integer customerId, Integer productId){
        cartRepository.deleteByCustomerAndProduct(customerId, productId);
    }

    public void deleteAllCartCustomer(Integer customerId){
        cartRepository.deleteCartByCustomer(customerId);
    }
}
