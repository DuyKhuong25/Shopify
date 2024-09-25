package com.ecom.admin.reponsitory.category;

import com.ecom.admin.repository.category.CategoryRepository;
import com.ecom.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CategoryTest {
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void createCategory(){
        Category category = new Category("Smartphone");
        categoryRepository.save(category);
    }

    @Test
    public void createSubCategory(){
        Category parent = new Category(2);
        Category laptop = new Category("Casio", parent);
        Category desktop = new Category("Rolex", parent);

        categoryRepository.saveAll(List.of(laptop, desktop));
    }

    @Test
    public void getCategory(){
        Category category = categoryRepository.findById(1).get();

        Set<Category> childCategory = category.getChildren();

        for(Category item : childCategory){
            System.out.println(item);
        }
    }

    @Test
    public void printCategory(){
        Iterable<Category> listCategory = categoryRepository.findAll();

        for(Category category : listCategory){
            if(category.getParent() == null){
                System.out.println(category.getName());
                Set<Category> childCategory = category.getChildren();
                for(Category subCategory : childCategory){
                    System.out.println("--" + subCategory.getName());
                    getSubCategory(subCategory, 1);
                }
            }
        }
    }

    public void getSubCategory(Category parent, Integer level){
        Integer newLevel = level + 1;
        System.out.println("Run");
        for(Category subCategory : parent.getChildren()){
            for(int i = 0; i < newLevel; i++){
                System.out.print("--");
            }
            System.out.println(subCategory.getName());
            getSubCategory(subCategory, newLevel);
        }
    }
}
