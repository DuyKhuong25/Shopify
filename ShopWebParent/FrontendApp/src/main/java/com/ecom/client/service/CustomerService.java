package com.ecom.client.service;

import com.ecom.client.repository.CustomerRepository;
import com.ecom.common.entity.AuthenticationType;
import com.ecom.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;

@Service
@Transactional
public class CustomerService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CustomerRepository customerRepository;

    public void registerCustomer(Customer customer) {
        encoderPassword(customer);
        customer.setEnabled(false);
        customer.setCreateTime(new Date());
        customer.setAuthenticationType(AuthenticationType.DATABASE);
        String codeVerify = generateStringRandom(64);
        customer.setVerificationCode(codeVerify);
        customerRepository.save(customer);
    }

    public void updateCustomerAccount(Customer customer){
        Customer customerDB = customerRepository.findById(customer.getId()).get();

        if(!customer.getPassword().isEmpty()){
            customerDB.setPassword(customer.getPassword());
            encoderPassword(customerDB);
        }

        customerDB.setFullName(customer.getFullName());

        customerRepository.save(customerDB);
    }

    public void encoderPassword(Customer customer) {
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
    }

    public String generateStringRandom(int length){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = length;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }

    public Boolean activeCustomer(String token){
        Customer customer = customerRepository.findByTokenVerify(token);
        if(customer == null || customer.isEnabled()){
            return false;
        }else {
            customerRepository.updateCustomerActive(customer.getId());
            return true;
        }
    }

    public void updateAuthenticationType(int id, AuthenticationType authenticationType){
        customerRepository.updateCustomerAuthenticationType(id, authenticationType);
    }

    public Customer getCustomerByEmail(String email){
        return customerRepository.findByEmail(email);
    }

    public void addNewCustomerOAuth2(String name, String email, AuthenticationType authenticationType){
        Customer customer = new Customer();
        customer.setFullName(name);
        customer.setEmail(email);
        customer.setEnabled(true);
        customer.setCreateTime(new Date());
        customer.setAuthenticationType(authenticationType);
        customer.setPassword("");
        customerRepository.save(customer);
    }
}
