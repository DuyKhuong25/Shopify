package com.ecom.client.service;

import com.ecom.common.entity.Cart;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutService {
    public CheckoutInfo prepareCheckout(List<Cart> cartItems) {
        CheckoutInfo checkoutInfo = new CheckoutInfo();
        Long paymentTotal = 0L;
        for (Cart cartItem : cartItems) {
            paymentTotal += cartItem.getSubTotalCartItem();
        }
        checkoutInfo.setPaymentTotal(paymentTotal);

        return checkoutInfo;
    }

}
