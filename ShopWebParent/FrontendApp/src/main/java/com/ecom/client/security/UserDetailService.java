package com.ecom.client.security;

import com.ecom.client.repository.CustomerRepository;
import com.ecom.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailService implements UserDetailsService {
    @Autowired
    CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email);
        if(customer != null) {
            return new UserDetail(customer);
        }else{
            throw new UsernameNotFoundException("Không tìm thấy người dùng với Email: " + email);
        }
    }
}
