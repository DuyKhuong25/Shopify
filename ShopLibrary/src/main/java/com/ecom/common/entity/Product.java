package com.ecom.common.entity;

import com.ecom.common.Constants;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 256, nullable = false, unique = true)
    private String name;

    @Column(length = 256, nullable = false, unique = true)
    private String alias;

    @Column(name = "short_description", length = 500, nullable = false)
    private String shortDescription;

    @Column(name = "full_description", length = 5000, nullable = false)
    private String fullDescription;

    @Column(name = "created_time")
    private Date createTime;

    @Column(name = "updated_time")
    private Date updateTime;

    @ColumnDefault("1")
    private boolean enabled;

    @ColumnDefault("1")
    @Column(name = "in_stock")
    private boolean inStock;

    @ColumnDefault("0")
    private int cost;

    @ColumnDefault("0")
    private long price;

    @ColumnDefault("0")
    @Column(name = "discount_percent")
    private int discountPercent;

    @ColumnDefault("0")
    private float length;

    @ColumnDefault("0")
    private float width;

    @ColumnDefault("0")
    private float height;

    @ColumnDefault("0")
    private float weight;

    @Column(name = "main_image", nullable = false)
    private String mainImage;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OrderBy("id ASC")
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductImage> images = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDetail> details = new ArrayList<>();

    @Column(name = "average_rating")
    private float averageRating;

    @Column(name = "count_rating")
    private int countRating;

    @Transient
    @Column(name = "is_rated")
    private boolean isRated;

    public Product() {}

    public Product(Integer id) {
        this.id = id;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public int getCountRating() {
        return countRating;
    }

    public void setCountRating(int countRating) {
        this.countRating = countRating;
    }

    public boolean isRated() {
        return isRated;
    }

    public void setRated(boolean rated) {
        isRated = rated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Set<ProductImage> getImages() {
        return images;
    }

    public void setImages(Set<ProductImage> images) {
        this.images = images;
    }

    public void addImage(String name) {
        this.images.add(new ProductImage(name, this));
    }

    public List<ProductDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ProductDetail> details) {
        this.details = details;
    }

    public void addDetail(String name, String value) {
        this.details.add(new ProductDetail(name, value, this));
    }

    public void addDetail(Integer id ,String name, String value) {
        this.details.add(new ProductDetail(id, name, value, this));
    }

    @Transient
    public Long getDiscountPrice(){
        long newPrice = (Long)(this.price * (100 - this.discountPercent)) / 100;
        return newPrice;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", brand=" + brand + ", category=" + category +"]";
    }

    public String getMainImagePath(){
        if(id == null || mainImage == null) return "/images/empty-thumb.png";
        return Constants.S3_URI + "product-image/" + this.id + "/" + this.mainImage;
    }
}
