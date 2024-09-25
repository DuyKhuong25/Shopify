package com.ecom.common.entity;

import com.ecom.common.Constants;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 64, nullable = false, unique = true)
    private String alias;

    @Column(length = 128)
    private String image;

    private boolean enabled;

    @Column(name = "all_parent_id", nullable = true)
    private String allParentId;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy("name ASC")
    private Set<Category> children = new HashSet<>();

    public Category() {
    }

    public Category(Integer id) {
        this.id = id;
    }

    public Category(String name) {
        this.name = name;
        this.alias = name;
    }

    public static Category copyIdAndName(Category category){
        Category copyCategory = new Category();
        copyCategory.setId(category.getId());
        copyCategory.setName(category.getName());

        return copyCategory;
    }

    public static Category copyIdAndName(Integer id, String name){
        Category copyCategory = new Category();
        copyCategory.setId(id);
        copyCategory.setName(name);

        return copyCategory;
    }

    public static Category copyFull(Category category){
        Category copyCategory = new Category();
        copyCategory.setId(category.getId());
        copyCategory.setAlias(category.getAlias());
        copyCategory.setName(category.getName());
        copyCategory.setEnabled(category.isEnabled());
        copyCategory.setImage(category.getImage());
        copyCategory.setHasChild(category.getChildren().size() > 0);

        return copyCategory;
    }

    public static Category copyFull(Category category, String name){
        Category copyCategory = Category.copyFull(category);
        copyCategory.setName(name);
        return copyCategory;
    }

    public Category(String name, Category parent) {
        this(name);
        this.parent = parent;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }

    public String getAllParentId() {
        return allParentId;
    }

    public void setAllParentId(String allParentId) {
        this.allParentId = allParentId;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Transient
    public String getCategoryImage(){
        if(id == null || image == null || image.isEmpty()) return "/images/empty-thumb.png";
        return Constants.S3_URI + "category-image/" + this.id + "/" + this.image;
    }

    @Transient
    private Boolean hasChild;

    public Boolean isHasChild(){
        return hasChild;
    }

    public void setHasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }


}
