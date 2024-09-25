package com.ecom.admin.controller;

import com.ecom.admin.AmazonS3Utility;
import com.ecom.admin.FileUpload;
import com.ecom.admin.exception.BrandNotFoundException;
import com.ecom.admin.service.BrandService;
import com.ecom.admin.service.CategoryService;
import com.ecom.common.entity.Brand;
import com.ecom.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

import static com.ecom.admin.service.BrandService.BRAND_PER_PAGE;

@Controller
public class BrandController {
    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/brands")
    public String listBrand(Model model) {
        return listBrandByPage(1, null, model);
    }

    @GetMapping("/brands/page/{pageNum}")
    public String listBrandByPage(@PathVariable("pageNum") Integer pageNum, @Param("keyword") String keyword ,Model model) {
        Page<Brand> pageBrands = brandService.getBrandByPage(pageNum, keyword);
        List<Brand> brands = pageBrands.getContent();

        long startItem = (pageNum - 1) * BRAND_PER_PAGE + 1;
        long endItem = startItem + BRAND_PER_PAGE - 1;
        if(endItem > pageBrands.getTotalElements()) {
            endItem = pageBrands.getTotalElements();
        }

        model.addAttribute("brands", brands);
        model.addAttribute("startItem", startItem);
        model.addAttribute("endItem", endItem);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPage", pageBrands.getTotalPages());
        model.addAttribute("totalItem", pageBrands.getTotalElements());

        return "brand/brand-list";
    }

    @GetMapping("/brands/add")
    public String addBrand(Model model) {
        List<Category> categories = categoryService.getListCategoryForm();

        model.addAttribute("brand", new Brand());
        model.addAttribute("categories", categories);
        model.addAttribute("title", "Thêm Brand");
        model.addAttribute("status", "add");
        return "brand/brand-form";
    }

    @PostMapping("/brands/save")
    public String saveBrand(@RequestParam(name = "logoUpload")MultipartFile multipartFile,
                            @ModelAttribute("brand") Brand brand,
                            RedirectAttributes redirectAttributes) throws IOException {
        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setLogo(fileName);
            Brand brandSave = brandService.saveBrand(brand);
            String uploadDir = "brand-logo/" + brandSave.getId();
//            FileUpload.cleanDir(uploadDir);
//            FileUpload.saveFile(uploadDir, fileName, multipartFile);
            AmazonS3Utility.removeFolder(uploadDir);
            AmazonS3Utility.uploadFileToS3(uploadDir, fileName, multipartFile.getInputStream());
        }else{
            if(brand.getLogo() == null) brand.setLogo(null);
            brandService.saveBrand(brand);
        }

        redirectAttributes.addFlashAttribute("message", "Save brand successfully!");
        return "redirect:/brands";
    }

    @GetMapping("/brands/edit/{id}")
    public String editBrand(Model model, @PathVariable(name = "id") Integer id) {
        Brand brand = brandService.getBrandById(id);
        List<Category> categories = categoryService.getListCategoryForm();

        model.addAttribute("brand", brand);
        model.addAttribute("categories", categories);
        model.addAttribute("title", "Cập nhật Brand");
        model.addAttribute("status", "edit");
        return "brand/brand-form";
    }

    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes){
        try{
            brandService.deleteBrand(id);
            String removeDir = "brand-logo/" + id;
//            FileUpload.removeDir(removeDir);
            AmazonS3Utility.removeFolder(removeDir);
            redirectAttributes.addFlashAttribute("message", "Delete brand successfully!");

        } catch (BrandNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/brands";
    }
}
