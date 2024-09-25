package com.ecom.admin.controller;

import com.ecom.admin.AmazonS3Utility;
import com.ecom.admin.FileUpload;
import com.ecom.admin.security.UserDetails;
import com.ecom.admin.service.UserService;
import com.ecom.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class UserAccountController {

    @Autowired
    UserService userService;

    @GetMapping("/users/account")
    public String userAccount(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String emailUser = userDetails.getUsername();
        User currentUser = userService.getUserByEmail(emailUser);
        model.addAttribute("user", currentUser);

        return "user/user-account";
    }

    @PostMapping("/users/update")
    public String saveUser(@ModelAttribute User user,
                           RedirectAttributes redirectAttributes,
                           @RequestParam(name = "image") MultipartFile multipartFile,
                           @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {

        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.updateAccount(user);
            String uploadDir = "user-photo/" + savedUser.getId();
//            FileUpload.cleanDir(uploadDir);
//            FileUpload.saveFile(uploadDir, fileName, multipartFile);

            AmazonS3Utility.removeFolder(uploadDir);
            AmazonS3Utility.uploadFileToS3(uploadDir, fileName, multipartFile.getInputStream());
        }else{
            if(user.getPhotos() == null) {user.setPhotos(null);}
            userService.updateAccount(user);
        }

        userDetails.setFirstName(user.getFirstName());
        userDetails.setLastName(user.getLastName());

        redirectAttributes.addFlashAttribute("message", "The user has been update successfully.");
        return "redirect:/users/account";
    }
}
