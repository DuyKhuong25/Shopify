package com.ecom.common.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 54, nullable = false)
    private String fullName;

    @Column(length = 54, nullable = false)
    private String email;

    @Column(length = 64, nullable = false)
    private String password;

    @ColumnDefault("true")
    private boolean enabled;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    @Column(name = "create_time")
    private Date createTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type", length = 15)
    private AuthenticationType authenticationType;

    public Customer(){}

    public Customer(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(AuthenticationType authCategory) {
        this.authenticationType = authCategory;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", password=" + password +
                ", verificationCode='" + verificationCode + '\'' +
                '}';
    }
}
