package com.ecom.admin.rest;

import com.ecom.admin.repository.brand.BrandRepository;
import com.ecom.admin.service.BrandService;
import com.ecom.admin.service.CategoryDataObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BrandRestController{
    @Autowired
    private BrandService brandService;

    @PostMapping("/brands/check_unique")
    public String checkBrandUnique(@Param("id") Integer id,
                                   @Param("name") String name){
        return brandService.checkBrandUnique(id, name);
    }

    @GetMapping("/brands/{id}/categories")
    public List<CategoryDataObject> getCategoryByBrandId(@PathVariable(name = "id") Integer id){
        return brandService.getCategoryByBrand(id);
    }
}
