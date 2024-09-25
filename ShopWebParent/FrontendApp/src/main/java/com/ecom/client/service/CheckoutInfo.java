package com.ecom.client.service;

import java.util.Calendar;
import java.util.Date;

public class CheckoutInfo {
    private long paymentTotal;
    private int deliverDays = 3;

    public int getDeliverDays() {
        return deliverDays;
    }

    public long getPaymentTotal() {
        return paymentTotal;
    }

    public void setPaymentTotal(long paymentTotal) {
        this.paymentTotal = paymentTotal;
    }

    public Date getDeliverDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, deliverDays);

        return calendar.getTime();
    }

}
