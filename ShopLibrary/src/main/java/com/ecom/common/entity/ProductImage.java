package com.ecom.common.entity;

import com.ecom.common.Constants;
import jakarta.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255)
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductImage() {}

    public ProductImage(String name, Product product) {
        this.name = name;
        this.product = product;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Transient
    public String getExtraImagePath(){
        return Constants.S3_URI + "product-image/" + this.product.getId() + "/extras/" + this.name;
    }
}
