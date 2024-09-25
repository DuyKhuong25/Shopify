package com.ecom.client.repository;

import com.ecom.common.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE c.enabled = true AND c.parent.id IS null")
    List<Category> findRootCategory();

    Category findByAlias(String alias);

}
