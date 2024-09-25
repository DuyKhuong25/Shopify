package com.ecom.admin.reponsitory.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncodeTest {
    @Test
    public void testPasswordEncode() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "khuong123";
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println(encodedPassword);
        Boolean mathches = passwordEncoder.matches(password, encodedPassword);
        System.out.println(mathches);
    }
}
