package com.ecom.admin.repository.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ecom.common.entity.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE c.parent.id IS null")
    Page<Category> getRootCategory(Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.name LIKE %?1%")
    Page<Category> searchCategory(String keyword, Pageable pageable);

    Long countById(@Param("id") Integer id);

    Category findByName(String name);

    Category findByAlias(String alias);

    @Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
    @Modifying
    void updateEnabled(Integer id, Boolean enabled);

}
