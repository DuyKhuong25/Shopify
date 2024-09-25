package com.ecom.admin.security;

import com.ecom.admin.repository.user.UserRepository;
import com.ecom.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email);
        if(user != null) {
            return new com.ecom.admin.security.UserDetails(user);
        }
        throw new UsernameNotFoundException("Not found user with email: " + email);
    }
}
