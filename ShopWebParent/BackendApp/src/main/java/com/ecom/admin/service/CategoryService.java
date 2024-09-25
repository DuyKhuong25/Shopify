package com.ecom.admin.service;

import com.ecom.admin.exception.CategoryNotFoundException;
import com.ecom.admin.repository.category.CategoryRepository;
import com.ecom.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CategoryService {
    public static final int CATEGORY_PER_PAGE = 3;

    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> getByPage(CategoryPageInfo categoryPageInfo, int page, String keyword){
        Pageable pageable = PageRequest.of(page - 1, CATEGORY_PER_PAGE);

        Page<Category> categories = null;
        List<Category> rootCategories = null;
        List<Category> searchCategory = null;

        if(keyword != null && !keyword.isEmpty()){
            categories = categoryRepository.searchCategory(keyword, pageable);
        }else{
            categories = categoryRepository.getRootCategory(pageable);
            rootCategories = categories.getContent();
        }

        categoryPageInfo.setTotalPage(categories.getTotalPages());
        categoryPageInfo.setTotalElements(categories.getTotalElements());

        if(keyword != null && !keyword.isEmpty()){
            searchCategory = categories.getContent();
            for(Category category : searchCategory){
                category.setHasChild(category.getChildren().size() > 0);
            }
            return searchCategory;
        }else{
            return listHierarchicalCategory(rootCategories);
        }
    }

    public List<Category> listHierarchicalCategory(List<Category> rootCategories){
        List<Category> listHierarchicalCategory = new ArrayList<>();
        for(Category category : rootCategories){
            listHierarchicalCategory.add(Category.copyFull(category));
            for(Category subCategory : category.getChildren()){
                String name = "→" + subCategory.getName();
                listHierarchicalCategory.add(Category.copyFull(subCategory, name));
                listSubHierarchicalCategory(listHierarchicalCategory, subCategory, 1);
            }
        }

        return listHierarchicalCategory;
    }

    public List<Category> listSubHierarchicalCategory(List<Category> listHierarchicalCategory, Category parent, Integer level){
        Integer currentLevel = level + 1;

        for(Category category : parent.getChildren()){
            String name = "";
            for(int i = 0; i < currentLevel; i++){
                name += "→";
            }
            name += category.getName();
            listHierarchicalCategory.add(Category.copyFull(category, name));
            listSubHierarchicalCategory(listHierarchicalCategory, category, currentLevel);
        }

        return listHierarchicalCategory;
    }

    public Category save(Category category){
        Category parent = category.getParent();
        if(category.getAlias() == null || category.getAlias().isEmpty()){
            category.setAlias(createSlug(category.getName()));
        }else{
            category.setAlias(createSlug(category.getAlias()));
        }

        if(parent != null){
            String allParentId = parent.getAllParentId() == null ? "-" : parent.getAllParentId();
            allParentId += parent.getId() + "-";
            category.setAllParentId(allParentId);
        }
        return categoryRepository.save(category);
    }

    public String createSlug(String input){

        input = input.trim();

        input = input
                .replaceAll("[()]", "")
                .replaceAll("[-]+", " ")
                .replaceAll("/", "");

        String slug = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("đ", "d")
                .replaceAll("Đ", "d");

        slug = slug.toLowerCase()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "");

        return slug;
    }

    public Category getCategory(int id) throws CategoryNotFoundException {
        try {
            Category category = categoryRepository.findById(id).get();
            return category;
        }catch (NoSuchElementException ex){
            throw new CategoryNotFoundException("Couldn't find category with this ID");
        }
    }

    public void deleteCategory(Integer id) throws CategoryNotFoundException {
        Long count = categoryRepository.countById(id);
        if(count == 0 || count == null){
            throw new CategoryNotFoundException("Couldn't find category with this ID");
        }
        categoryRepository.deleteById(id);
    }

    public void updateCategoryStatus(Integer id, Boolean enable){
        categoryRepository.updateEnabled(id, enable);
    }

    public String checkUniqueCategory(Integer id, String name, String alias){
        Boolean isCreateCategory = (id == null);
        Category categoryByName = categoryRepository.findByName(name);
        Category categoryByAlias = categoryRepository.findByAlias(alias);

        if(categoryByAlias == null || categoryByName == null){
            return "OK";
        }

        if(isCreateCategory){
            if(categoryByName != null){
                return "DuplicateName";
            } else if (categoryByAlias != null) {
                return "DuplicateAlias";
            }
        }else{
            if(categoryByName.getId() != id) {
                return "DuplicateName";
            } else if (categoryByAlias.getId() != id) {
                return "DuplicateAlias";
            }
        }
        return "OK";
    }

    public List<Category> getListCategoryForm(){
        List<Category> categoryListForm = new ArrayList<Category>();
        List<Category> categoryListDB = categoryRepository.findAll();

        for (Category category : categoryListDB) {
            if(category.getParent() == null){
                categoryListForm.add(Category.copyIdAndName(category));
                for(Category subCategory : category.getChildren()){
                    String name = "→ " + subCategory.getName();
                    categoryListForm.add(Category.copyIdAndName(subCategory.getId(), name));
                    getSubCategory(categoryListForm, subCategory, 1);
                }
            }
        }

        return categoryListForm;
    }

    public void getSubCategory(List<Category> categoryListForm, Category parent, Integer level){
        Integer newLevel = level + 1;
        for(Category subCategory : parent.getChildren()){
            String name = "";
            for(int i = 0; i < newLevel; i++){
                name += "→ ";
            }
            name += subCategory.getName();
            categoryListForm.add(Category.copyIdAndName(subCategory.getId(), name));
            getSubCategory(categoryListForm, subCategory, newLevel);
        }
    }
}
