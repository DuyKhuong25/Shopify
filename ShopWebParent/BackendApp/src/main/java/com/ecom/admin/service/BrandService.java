package com.ecom.admin.service;

import com.ecom.admin.exception.BrandNotFoundException;
import com.ecom.admin.repository.brand.BrandRepository;
import com.ecom.common.entity.Brand;
import com.ecom.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class BrandService {
    public static Integer BRAND_PER_PAGE = 8;

    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public Page<Brand> getBrandByPage(Integer pageNum, String keyword){
        Pageable pageable = PageRequest.of(pageNum - 1, BRAND_PER_PAGE);
        if(keyword != null){
            return brandRepository.getBrandsSearch(keyword, pageable);
        }
        return brandRepository.findAll(pageable);
    }

    public void deleteBrand(Integer id) throws BrandNotFoundException {
        long countBrand = brandRepository.countById(id);
        if(countBrand < 0 || countBrand == 0){
            throw new BrandNotFoundException("Couldn't find brand with ID");
        }
        brandRepository.deleteById(id);

    }

    public List<CategoryDataObject> getCategoryByBrand(Integer id){
        List<CategoryDataObject> categoryData = new ArrayList<>();
        Brand brand = brandRepository.findById(id).get();
        Set<Category> listCategory = brand.getCategories();
        for(Category category : listCategory){
            if(category.getParent() == null || category.getChildren() != null){
                categoryData.add(new CategoryDataObject(category.getId(), category.getName()));
                getSubCategory(categoryData, category, 1);
            }
        }

        return categoryData;
    }

    public void getSubCategory(List<CategoryDataObject> categoryData, Category parent, Integer level){
        for(Category childCategory : parent.getChildren()){
            String name = "";
            for(int i = 0; i < level; i++){
                name += "→ ";
            }
            name += childCategory.getName();
            categoryData.add(new CategoryDataObject(childCategory.getId(), name));
            getSubCategory(categoryData, childCategory, level + 1);
        }
    }

    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public Brand getBrandById(int id) {
        return brandRepository.findById(id).get();
    }

    public String checkBrandUnique(Integer id, String name) {
        Brand brand = brandRepository.getBrandByName(name);
        Boolean isCreateBrand = (id == null);

        if (brand == null) {return "OK";}

        if(isCreateBrand){
            if(brand != null){return "Duplicated";}
        }else{
            if(brand.getId() != id){return "Duplicated";}
        }

        return "OK";
    }
}
