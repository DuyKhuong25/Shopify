package com.ecom.common.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "name_customer", length = 50, nullable = false)
    private String nameCustomer;

    @Column(name = "phone_customer", length = 15, nullable = false)
    private String phoneCustomer;

    @Column(name = "city", length = 50, nullable = false)
    private String city;

    @Column(name = "district", length = 50, nullable = false)
    private String district;

    @Column(name = "ward", length = 50, nullable = false)
    private String ward;

    @Column(name = "detail_address", length = 150, nullable = false)
    private String detailAddress;

    @Column(name = "address_default")
    private boolean addressDefault;

    public Address() {}

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public String getPhoneCustomer() {
        return phoneCustomer;
    }

    public void setPhoneCustomer(String phoneCustomer) {
        this.phoneCustomer = phoneCustomer;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public boolean isAddressDefault() {
        return addressDefault;
    }

    public void setAddressDefault(boolean addressDefault) {
        this.addressDefault = addressDefault;
    }

    @Transient
    public String getInfoCustomerAddress(){
        return this.nameCustomer + " | " + this.phoneCustomer + " | " + this.detailAddress + ", " + this.ward + ", " + this.district + ", " + this.city;
    }

    @Transient
    public String getStringAddress(){
        return this.detailAddress + ", " + this.ward + ", " + this.district + ", " + this.city;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressDefault=" + addressDefault +
                ", id=" + id +
                ", customer=" + customer +
                ", nameCustomer='" + nameCustomer + '\'' +
                ", phoneCustomer='" + phoneCustomer + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", ward='" + ward + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                '}';
    }
}
