package com.ecom.admin.controller;

import com.ecom.admin.AmazonS3Utility;
import com.ecom.admin.FileUpload;
import com.ecom.admin.exception.UserNotFoundException;
import com.ecom.admin.repository.user.RoleRepository;
import com.ecom.admin.service.UserService;
import com.ecom.common.entity.Role;
import com.ecom.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

import static com.ecom.admin.service.UserService.USER_PER_PAGE;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/users")
    public String usersList(Model model) {
        return userListPage(1, model);
    }

    @GetMapping("/users/page/{pageNum}")
    public String userListPage(@PathVariable(name = "pageNum") int pageNum, Model model) {
        Page<User> users = userService.getByPage(pageNum);
        List<User> userList = users.getContent();
        long startItem = (pageNum -1) * USER_PER_PAGE + 1;
        long endItem = startItem + USER_PER_PAGE - 1;
        if(endItem > users.getTotalElements()) {endItem = users.getTotalElements();}

        model.addAttribute("startItem", startItem);
        model.addAttribute("endItem", endItem);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPage", users.getTotalPages());
        model.addAttribute("totalItem", users.getTotalElements());
        model.addAttribute("listUsers", userList);
        return "user/users";
    }

    @GetMapping("/users/add")
    public String addUser(Model model) {
        User user = new User();
        user.setEnabled(true);
        List<Role> roles = roleRepository.findAll();

        model.addAttribute("title", "Thêm người dùng");
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("status", "add");

        return "user/user-form";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute User user,
                           RedirectAttributes redirectAttributes,
                           @RequestParam(name = "image") MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.save(user);
            String uploadDir = "user-photo/" + savedUser.getId();
//            FileUpload.cleanDir(uploadDir);
//            FileUpload.saveFile(uploadDir, fileName, multipartFile);
            AmazonS3Utility.removeFolder(uploadDir);
            AmazonS3Utility.uploadFileToS3(uploadDir, fileName, multipartFile.getInputStream());
        }else{
            if(user.getPhotos() == null) {user.setPhotos(null);}
            userService.save(user);
        }

        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try{
            User user = userService.get(id);
            List<Role> roles = roleRepository.findAll();

            model.addAttribute("user", user);
            model.addAttribute("roles", roles);
            model.addAttribute("title", "Cập nhật người dùng");
            model.addAttribute("status", "edit");

            return "user/user-form";
        }catch (UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id,
                             RedirectAttributes redirectAttributes) {

        try{
            userService.delete(id);
            String removeDir = "user-photo/" + id;
            AmazonS3Utility.removeFolder(removeDir);

            redirectAttributes.addFlashAttribute("message", "The user has been deleted successfully.");
        }catch(UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enable/{status}")
    public String updateEnabledStatus(@PathVariable(name = "id") Integer id,
                                      @PathVariable(name = "status") Boolean enable,
                                      RedirectAttributes redirectAttributes) {
        userService.updateEnabledStatus(id, enable);
        String status = enable ? "enable" : "disable";
        redirectAttributes.addFlashAttribute("message", "The user status has been updated "+ status +" successfully.");
        return "redirect:/users";
    }
}
