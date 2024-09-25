package com.ecom.admin.controller;

import com.ecom.admin.AmazonS3Utility;
import com.ecom.admin.FileUpload;
import com.ecom.admin.exception.CategoryNotFoundException;

import com.ecom.admin.service.CategoryPageInfo;
import com.ecom.admin.service.CategoryService;
import com.ecom.common.entity.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.ecom.admin.service.CategoryService.CATEGORY_PER_PAGE;

@Controller
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/categories")
    public String getCategory(Model model) {
        return getCategoryPage(1, null, model);
    }

    @GetMapping("/categories/page/{pageNum}")
    public String getCategoryPage(@PathVariable("pageNum") Integer pageNum,
                                  @Param("keyword") String keyword,
                                  Model model) {
        CategoryPageInfo categoryPageInfo = new CategoryPageInfo();
        List<Category> categories = categoryService.getByPage(categoryPageInfo, pageNum, keyword);
        long startItem = (pageNum - 1) * CATEGORY_PER_PAGE + 1;
        long endItem = startItem + CATEGORY_PER_PAGE - 1;
        if(endItem > categoryPageInfo.getTotalElements()){
            endItem = categoryPageInfo.getTotalElements();
        }

        int totalPage = categoryPageInfo.getTotalPage();
        long totalElements = categoryPageInfo.getTotalElements();

        model.addAttribute("startItem", startItem);
        model.addAttribute("endItem", endItem);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("totalItem", totalElements);
        model.addAttribute("categories", categories);
        return "category/category-list";
    }

    @GetMapping("/categories/add")
    public String addCategory(Model model) {
        Category category = new Category();
        List<Category> categories = categoryService.getListCategoryForm();
        category.setEnabled(true);
        model.addAttribute("category", category);
        model.addAttribute("status", "add");
        model.addAttribute("categories", categories);
        model.addAttribute("title", "Thêm danh mục");

        return "category/category-form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@ModelAttribute Category category,
                               @RequestParam(name = "imageUpload") MultipartFile multipartFile,
                               RedirectAttributes redirectAttributes) throws IOException {

        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);
            Category categorySave = categoryService.save(category);
            String uploadDir = "category-image/" + categorySave.getId();
//            FileUpload.cleanDir(uploadDir);
//            FileUpload.saveFile(uploadDir, fileName, multipartFile);
            AmazonS3Utility.removeFolder(uploadDir);
            AmazonS3Utility.uploadFileToS3(uploadDir, fileName, multipartFile.getInputStream());
        }else{
            if(category.getImage() == null) category.setImage(null);
            categoryService.save(category);
        }
        redirectAttributes.addFlashAttribute("message", "Save category successfully!");
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes){
        try{
            Category category = categoryService.getCategory(id);
            List<Category> listCategory = categoryService.getListCategoryForm();

            model.addAttribute("category", category);
            model.addAttribute("categories", listCategory);
            model.addAttribute("title", "Cập nhật danh mục");
            model.addAttribute("status", "edit");

            return "category/category-form";
        }catch (CategoryNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        try{
            categoryService.deleteCategory(id);
            String removeDir = "category-image/" + id;
//            FileUpload.removeDir(uploadDir);
            AmazonS3Utility.removeFolder(removeDir);
            redirectAttributes.addFlashAttribute("message", "Delete category successfully!");
        }catch (CategoryNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/categories";
    }


    @GetMapping("/categories/{id}/enable/{status}")
    public String updateCategoryEnable(@PathVariable(name = "id") Integer id,
                                       @PathVariable("status") Boolean status,
                                       RedirectAttributes redirectAttributes){
        categoryService.updateCategoryStatus(id, status);
        redirectAttributes.addFlashAttribute("message", "Update category enable successfully!");
        return "redirect:/categories";
    }
}
