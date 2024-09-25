package com.ecom.client.service;

import com.ecom.client.repository.CategoryRepository;
import com.ecom.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> findRootCategory() {
        return categoryRepository.findRootCategory();
    }

    public List<Category> getSubCategories(Category category) {
        List<Category> listSubCategory = new ArrayList<>();
        if(category.getChildren() != null){
            for (Category subCategory : category.getChildren()){
                listSubCategory.add(subCategory);
            }
        }
        return listSubCategory;
    }

    public Category findCategoryByAlias(String alias) {
        return categoryRepository.findByAlias(alias);
    }
}
