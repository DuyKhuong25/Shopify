package com.ecom.admin.repository.brand;

import com.ecom.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    @Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
    List<Brand> findAllBrands();

    Brand getBrandByName(String name);

    Long countById(@Param("id") Integer id);

    @Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
    Page<Brand> getBrandsSearch(String keyword, Pageable pageable);
}
