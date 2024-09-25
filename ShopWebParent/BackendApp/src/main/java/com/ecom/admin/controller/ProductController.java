package com.ecom.admin.controller;

import com.ecom.admin.AmazonS3Utility;
import com.ecom.admin.FileUpload;
import com.ecom.admin.exception.ProductNotFoundException;
import com.ecom.admin.service.BrandService;
import com.ecom.admin.service.CategoryService;
import com.ecom.admin.service.ProductService;
import com.ecom.common.entity.Brand;
import com.ecom.common.entity.Category;
import com.ecom.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.lang.model.element.Name;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.ecom.admin.service.ProductService.PRODUCT_PER_PAGE;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    BrandService brandService;

    @GetMapping("/products")
    public String listProducts(Model model) {
        return listProductByPage(1, null, null, model);
    }

    @GetMapping("/products/page/{pageNum}")
    public String listProductByPage(@PathVariable(name = "pageNum") Integer pageNum,
                                    @Param("keyword") String keyword,
                                    @Param("category") Integer category,
                                    Model model){
        Page<Product> productPage = productService.findProductByPage(pageNum, keyword, category);
        List<Product> products = productPage.getContent();
        List<Category> categories = categoryService.getListCategoryForm();

        long startItem = (pageNum - 1) * PRODUCT_PER_PAGE + 1;
        long endItem = startItem + PRODUCT_PER_PAGE - 1;
        long totalPage = productPage.getTotalPages();
        long totalItem = productPage.getTotalElements();

        model.addAttribute("products", products);
        model.addAttribute("startItem", startItem);
        model.addAttribute("endItem", endItem);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("totalItem", totalItem);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryID", category);
        model.addAttribute("title", "Danh sách sản phẩm");
        return "product/product-list";
    }

    @GetMapping("/products/add")
    public String addProducts(Model model) {
        Product product = new Product();
        List<Brand> brands = brandService.getAllBrands();
        model.addAttribute("product", product);
        model.addAttribute("brands", brands);
        model.addAttribute("status", "add");
        model.addAttribute("title", "Thêm sản phẩm");
        return "product/product-form";
    }

    @GetMapping("/products/edit/{id}")
    public String editProducts(@PathVariable("id") int id,
                               Model model,
                               RedirectAttributes redirectAttributes) throws ProductNotFoundException {
        try {
            Product product = productService.getProductById(id);
            List<Brand> brands = brandService.getAllBrands();
            model.addAttribute("product", product);
            model.addAttribute("brands", brands);
            model.addAttribute("status", "edit");


            return "product/product-form";

        }catch (ProductNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/products";
        }
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam(name = "mainImageUpload") MultipartFile mainImageMultipart,
                              @RequestParam(name = "subImageUpload") MultipartFile[] extraImageMultipart,
                              @RequestParam(name = "detailName", required = false) String[] detailName,
                              @RequestParam(name = "detailID", required = false) Integer[] detailID,
                              @RequestParam(name = "detailValue", required = false) String[] detailValue,
                              RedirectAttributes redirectAttributes) throws IOException {

        setMainImageName(mainImageMultipart, product);
        setExtraImageNames(extraImageMultipart, product);
        setProductDetail(detailID, detailName, detailValue, product);

        Product savedProduct = productService.save(product);
        productService.updateCountAndAverageRating(product.getId());

        saveUploadedImages(mainImageMultipart, extraImageMultipart, savedProduct);

        redirectAttributes.addFlashAttribute("message", "Save product successfully!");
        return "redirect:/products";
    }

    private void setProductDetail(Integer[] detailID, String[] name, String[] values, Product product){
        if(name != null || name.length > 0){
            for (int i = 0; i < name.length; i++) {
                String nameDetail = name[i];
                String valueDetail = values[i];
                Integer idDetail = detailID[i];
                if(!nameDetail.isEmpty() && !valueDetail.isEmpty()){
                    if(idDetail != 0){
                        product.addDetail(idDetail, nameDetail, valueDetail);
                    }else if(idDetail == 0){
                        product.addDetail(nameDetail, valueDetail);
                    }
                }
            }
        }

    }

    private void saveUploadedImages(MultipartFile mainImageMultipart,
                                    MultipartFile[] extraImageMultipart, Product savedProduct) throws IOException {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
            String uploadDir = "product-image/" + savedProduct.getId();

            List<String> listObjectKey = AmazonS3Utility.listFolder(uploadDir);
            for (String objectKey : listObjectKey){
                if(!objectKey.contains("/extras/")){
                    AmazonS3Utility.deleteFile(objectKey);
                }
            }

            AmazonS3Utility.uploadFileToS3(uploadDir, fileName, mainImageMultipart.getInputStream());
//            FileUpload.cleanDir(uploadDir);
//            FileUpload.saveFile(uploadDir, fileName, mainImageMultipart);
        }

        if (extraImageMultipart.length > 0 && !extraImageMultipart[0].isEmpty()) {
            String uploadDir = "product-image/" + savedProduct.getId() + "/extras";

            for (MultipartFile extraMultipartFile : extraImageMultipart) {
                String fileName = StringUtils.cleanPath(extraMultipartFile.getOriginalFilename());
//                FileUpload.saveFile(uploadDir, fileName, extraMultipartFile);
                AmazonS3Utility.uploadFileToS3(uploadDir, fileName, extraMultipartFile.getInputStream());
            }
        }

    }

    private void setExtraImageNames(MultipartFile[] extraImageMultipart, Product product) {
        if (extraImageMultipart.length > 0 && !extraImageMultipart[0].isEmpty()) {
            for (MultipartFile multipartFile : extraImageMultipart) {
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                product.addImage(fileName);
            }
        }
    }

    private void setMainImageName(MultipartFile mainImageMultipart, Product product) {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
            product.setMainImage(fileName);
        }
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Integer id,
                                RedirectAttributes redirectAttributes) throws ProductNotFoundException {
        try {
            productService.deleteProduct(id);
            AmazonS3Utility.removeFolder("product-image/" + id);
            AmazonS3Utility.removeFolder("product-image/" + id + "/extras");
//            FileUpload.removeDir("product-image/" + id);
//            FileUpload.removeDir("product-image/" + id + "/extras");
        redirectAttributes.addFlashAttribute("message", "Delete product successfully!");
        }catch (ProductNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/enable/{status}")
    public String updateEnableProduct(@PathVariable(name = "id") Integer id,
                                      @PathVariable(name = "status") Boolean status,
                                      RedirectAttributes redirectAttributes){
        productService.updateEnableProduct(id, status);
        redirectAttributes.addFlashAttribute("message", "Update product status successfully!");
        return "redirect:/products";
    }

}
