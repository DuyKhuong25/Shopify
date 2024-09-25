package com.ecom.common.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name", nullable = false, length = 45)
    private String fullName;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "address", nullable = false, length = 150)
    private String address;

    private Date orderTime;

    private long total;

    private Date deliverDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OrderBy("id ASC")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    @OrderBy("id ASC")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderTrack> orderTracks = new ArrayList<>();

    public Order(){}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(Date deliverDate) {
        this.deliverDate = deliverDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(Set<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<OrderTrack> getOrderTracks() {
        return orderTracks;
    }

    public void setOrderTracks(List<OrderTrack> orderTracks) {
        this.orderTracks = orderTracks;
    }

    public void addOrderTrack(OrderTrack orderTrack) {
        this.orderTracks.add(orderTrack);
    }

    @Transient
    public void copyAddressCustomer(Address address) {
        this.fullName = address.getNameCustomer();
        this.phoneNumber = address.getPhoneCustomer();
        this.address = address.getStringAddress();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", customer=" + customer.getFullName() +
                ", fullName='" + fullName + '\'' +
                ", orderTime=" + orderTime +
                ", paymentMethod=" + paymentMethod +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", status=" + status +
                ", total=" + total +
                '}';
    }
}
