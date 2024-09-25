package com.ecom.admin.reponsitory.brand;

import com.ecom.admin.repository.brand.BrandRepository;
import com.ecom.common.entity.Brand;
import com.ecom.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {
    @Autowired
    BrandRepository brandRepository;

    @Test
    public void testCreateBrand() {
        Brand brand = new Brand("Acer");
        Category category1 = new Category(5);
        Category category2 = new Category(7);

        brand.addCategory(category1);
        brand.addCategory(category2);

        System.out.println(brand);

        brandRepository.save(brand);
    }

}
