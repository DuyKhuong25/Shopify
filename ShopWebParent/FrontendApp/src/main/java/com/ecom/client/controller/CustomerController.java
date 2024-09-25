package com.ecom.client.controller;

import com.ecom.client.MailConfig;
import com.ecom.client.exception.CustomerNotFoundException;
import com.ecom.client.service.*;
import com.ecom.common.entity.Address;
import com.ecom.common.entity.Cart;
import com.ecom.common.entity.Category;
import com.ecom.common.entity.Customer;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    SettingService settingService;

    @Autowired
    AddressService addressService;

    @Autowired
    CartService cartService;

    @PostMapping("/register/create_customer")
    public String createCustomer(@ModelAttribute Customer customer, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        customerService.registerCustomer(customer);
        sendVerifyEmail(request, customer);
        return "/form/register-success";
    }

    @GetMapping("/verify")
    public String activeCustomer(@Param("token") String token){
        boolean isActive = customerService.activeCustomer(token);
        return "/form/" + (isActive ? "verify-success" : "verify-fail");
    }

    @GetMapping("/account")
    public String getUserAccount(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Integer quantityCart = getQuantityCart(request);
        List<Category> rootCategory = categoryService.findRootCategory();
        Customer customer = getAuthenticatedCustomer(request);
        Address addressChoose = addressService.getCustomerDefaultAddress(customer);

        model.addAttribute("quantityCart", quantityCart);
        model.addAttribute("addressChoose", addressChoose);
        model.addAttribute("customer", customer);
        model.addAttribute("rootCategory", rootCategory);

        return "/account/general-user";
    }

    @PostMapping("/account/update")
    public String updateCustomerAccount(@ModelAttribute Customer customer){
        customerService.updateCustomerAccount(customer);
        return "redirect:/account";
    }

    @GetMapping("/account/address")
    public String getUserAddress(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Integer quantityCart = getQuantityCart(request);
        Customer customer = getAuthenticatedCustomer(request);

        List<Category> rootCategory = categoryService.findRootCategory();
        List<Address> listAddress = addressService.findAddressByCustomer(customer);

        model.addAttribute("quantityCart", quantityCart);
        model.addAttribute("rootCategory", rootCategory);
        model.addAttribute("listAddress", listAddress);

        return "/account/address-user";
    }

    @GetMapping("/account/address/add")
    public String addUserAddress(Model model){
        List<Category> rootCategory = categoryService.findRootCategory();
        Address address = new Address();

        model.addAttribute("address", address);
        model.addAttribute("rootCategory", rootCategory);

        return "/account/address-form";
    }

    @PostMapping("/account/address/save")
    public String saveUserAddress(@ModelAttribute Address address, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        address.setCustomer(customer);
        addressService.saveAddress(address, customer);

        return "redirect:/account/address";
    }

    @GetMapping("/account/address/edit/{addressId}")
    public String editUserAddressDefault(@PathVariable(name = "addressId") Integer addressId, Model model,HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        List<Category> rootCategory = categoryService.findRootCategory();
        Address address = addressService.findAddressByIdAndCustomer(addressId, customer);
        model.addAttribute("address", address);
        model.addAttribute("rootCategory", rootCategory);

        return "/account/address-form";
    }

    @GetMapping("/account/address/default/{addressId}")
    public String updateUserAddressDefault(@PathVariable(name = "addressId") Integer addressId, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);

        String redirectOption = request.getParameter("redirect");
        String redirectURL = "redirect:/account/address";

        if("checkout".equals(redirectOption)){
            redirectURL = "redirect:/checkout";
        }
        addressService.updateDefaultAddress(addressId, customer.getId());

        return redirectURL;
    }

    @GetMapping("/account/address/delete/{addressId}")
    public String deleteUserAddress(@PathVariable(name = "addressId") Integer addressId, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        addressService.deleteAddress(addressId, customer.getId());

        return "redirect:/account/address";
    }

    public Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = MailConfig.getEmailOfAuthenticatedCustomer(request);
        if (email == null) {
            throw new CustomerNotFoundException("Customer not found!");
        }
        return customerService.getCustomerByEmail(email);
    }

    public void sendVerifyEmail(HttpServletRequest request, Customer customer) throws MessagingException, UnsupportedEncodingException {
        EmailSettingPagkage emailSettingPagkage = settingService.getEmailSettingPagkage();
        JavaMailSenderImpl mailSender = MailConfig.getJavaMailSender(emailSettingPagkage);

        MimeMessage mimeMailMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

        String toAddress = customer.getEmail();
        String subject = emailSettingPagkage.getVerifySubject();
        String content = emailSettingPagkage.getVerifyContent();

        mimeMessageHelper.setFrom(emailSettingPagkage.getFromAddress(), emailSettingPagkage.getSenderName());
        mimeMessageHelper.setTo(toAddress);
        mimeMessageHelper.setSubject(subject);

        content = content.replace("[[Name]]", customer.getFullName());
        String verifyURL = MailConfig.getClientURL(request) + "/verify?token=" + customer.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);

        mimeMessageHelper.setText(content, true);
        mailSender.send(mimeMailMessage);
    }

    public Integer getQuantityCart(HttpServletRequest request) {
        Integer quantityCart = 0;

        String email = MailConfig.getEmailOfAuthenticatedCustomer(request);
        Customer customer = customerService.getCustomerByEmail(email);
        if(customer == null){
            quantityCart = 0;
        }else{
            List<Cart> cartOfCustomer = cartService.getCartByCustomer(customer.getId());
            for(Cart cart : cartOfCustomer){
                quantityCart += cart.getQuantity();
            }
        }

        return quantityCart;
    }
}
