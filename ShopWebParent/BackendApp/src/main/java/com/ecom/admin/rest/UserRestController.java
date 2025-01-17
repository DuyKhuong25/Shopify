package com.ecom.admin.rest;

import com.ecom.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/users/check_email")
    public String checkEmail(@Param("id") Integer id, @Param("email") String email) {
        return userService.checkEmailUnique(id, email) ? "OK" : "Duplicated";
    }
}
