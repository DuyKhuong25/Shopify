package com.ecom.client;

import com.ecom.client.security.oauth2.CustomerOAuth2User;
import com.ecom.client.service.EmailSettingPagkage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.Properties;

public class MailConfig {
    public static String getClientURL(HttpServletRequest request) {
        String clientURL = request.getRequestURL().toString();
        clientURL = clientURL.replace(request.getServletPath(), "");
        return clientURL;
    }

    public static JavaMailSenderImpl getJavaMailSender(EmailSettingPagkage emailSettingPagkage) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailSettingPagkage.getHost());
        mailSender.setPort(emailSettingPagkage.getPort());
        mailSender.setUsername(emailSettingPagkage.getUsername());
        mailSender.setPassword(emailSettingPagkage.getPassword());
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", emailSettingPagkage.getSmtpAuth());
        props.setProperty("mail.smtp.starttls.enable", emailSettingPagkage.getSmtpSecured());
        mailSender.setJavaMailProperties(props);

        return mailSender;
    }

    public static String getEmailOfAuthenticatedCustomer(HttpServletRequest httpServletRequest){
        Object principal = httpServletRequest.getUserPrincipal();
        if(principal == null) return null;

        String customerEmail = null;

        if(principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken){
            customerEmail = httpServletRequest.getUserPrincipal().getName();
        }else if(principal instanceof OAuth2AuthenticationToken){
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
            CustomerOAuth2User oAuth2User = (CustomerOAuth2User) oAuth2AuthenticationToken.getPrincipal();
            customerEmail = oAuth2User.getEmail();
        }

        return customerEmail;
    }

}
