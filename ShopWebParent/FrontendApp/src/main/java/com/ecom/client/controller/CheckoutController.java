package com.ecom.client.controller;

import com.ecom.client.MailConfig;
import com.ecom.client.exception.CustomerNotFoundException;
import com.ecom.client.service.*;
import com.ecom.common.entity.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@Controller
public class CheckoutController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    CartService cartService;

    @Autowired
    CustomerService customerService;

    @Autowired
    CheckoutService checkoutService;

    @Autowired
    AddressService addressService;

    @Autowired
    OrderService orderService;

    @Autowired
    SettingService settingService;

    @Autowired
    TemplateEngine templateEngine;

    @GetMapping("/checkout")
    public String checkout(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Integer quantityCart = getQuantityCart(request);
        Customer customer = getAuthenticatedCustomer(request);

        List<Category> rootCategory = categoryService.findRootCategory();
        List<Cart> listCart = cartService.getCartByCustomer(customer.getId());

        Address addressChoose = addressService.getCustomerDefaultAddress(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(listCart);

        model.addAttribute("quantityCart", quantityCart);
        model.addAttribute("rootCategory", rootCategory);

        model.addAttribute("listCart", listCart);
        model.addAttribute("checkoutInfo", checkoutInfo);
        model.addAttribute("addressChoose", addressChoose);

        return "cart/checkout";
    }

    @PostMapping("/checkout/save")
    public String createOrder(HttpServletRequest request) throws CustomerNotFoundException, MessagingException, UnsupportedEncodingException {
        String paymentType = request.getParameter("paymentMethod");
        PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);

        Customer customer = getAuthenticatedCustomer(request);
        Address addressChoose = addressService.getCustomerDefaultAddress(customer);
        List<Cart> listCartItem = cartService.getCartByCustomer(customer.getId());
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(listCartItem);

        Context context = new Context();
        context.setVariable("listCartItem", listCartItem);
        String orderProductContent = templateEngine.process("mail-order", context);

        Order newOrder = orderService.createOrder(customer, addressChoose, listCartItem, paymentMethod, checkoutInfo);
        sendMailOrderConfirm(customer, newOrder, paymentMethod, addressChoose, checkoutInfo, orderProductContent);
        cartService.deleteAllCartCustomer(customer.getId());

        return "cart/checkout-success";
    }

    public void sendMailOrderConfirm(Customer customer, Order order, PaymentMethod paymentMethod, Address address, CheckoutInfo checkoutInfo, String orderProductContent) throws MessagingException, UnsupportedEncodingException {
        EmailSettingPagkage emailSettingPagkage = settingService.getEmailSettingPagkage();
        JavaMailSenderImpl javaMailSender = MailConfig.getJavaMailSender(emailSettingPagkage);

        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

        mimeMessageHelper.setFrom(emailSettingPagkage.getFromAddress(), emailSettingPagkage.getSenderName());
        mimeMessageHelper.setTo(customer.getEmail());
        mimeMessageHelper.setSubject(emailSettingPagkage.getOrderSubject());

        String content = emailSettingPagkage.getOrderContent();

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String dateOrder = dateFormat.format(order.getOrderTime());
        String dateDelivery = dateFormat.format(order.getDeliverDate());

        content = content.replace("[[Name_customer]]", customer.getFullName());
        content = content.replace("[[Date_order]]", dateOrder);
        content = content.replace("[[Date_delivery]]", dateDelivery);
        content = content.replace("[[List_product]]", orderProductContent);
        content = content.replace("[[Address_order]]", address.getInfoCustomerAddress());
        content = content.replace("[[Type_payment]]", getPaymentMethod(paymentMethod));
        content = content.replace("[[Total_payment]]", numberFormat.format(checkoutInfo.getPaymentTotal()) + " VNĐ");

        mimeMessageHelper.setText(content, true);

        javaMailSender.send(mimeMailMessage);
    }

    public String getPaymentMethod(PaymentMethod paymentMethod){
        if(paymentMethod == PaymentMethod.CREDIT_CART){
            return "Thanh toán chuyển khoản ngân hàng.";
        }

        return "Thanh toán khi nhận hàng.";
    }

    public Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = MailConfig.getEmailOfAuthenticatedCustomer(request);
        if (email == null) {
            throw new CustomerNotFoundException("Customer not found!");
        }
        return customerService.getCustomerByEmail(email);
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
