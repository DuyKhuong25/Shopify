package com.ecom.client.security.oauth2;

import com.ecom.client.service.CustomerService;
import com.ecom.common.entity.AuthenticationType;
import com.ecom.common.entity.Customer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandle extends SavedRequestAwareAuthenticationSuccessHandler{
    @Autowired
    @Lazy
    CustomerService customerService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CustomerOAuth2User oauth2User = (CustomerOAuth2User) authentication.getPrincipal();
        String name = oauth2User.getName();
        String email = oauth2User.getEmail();
        String clientName = oauth2User.getClientName();

        Customer customer = customerService.getCustomerByEmail(email);
        AuthenticationType authenticationType = getAuthenticationTypeByClientName(clientName);

        if(customer == null){
            customerService.addNewCustomerOAuth2(name, email, authenticationType);
        }else{
            customerService.updateAuthenticationType(customer.getId(), authenticationType);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

    public AuthenticationType getAuthenticationTypeByClientName(String clientName){
        if(clientName.equals("Google")){
            return AuthenticationType.GOOGLE;
        }else if(clientName.equals("Facebook")){
            return AuthenticationType.FACEBOOK;
        }else{
            return AuthenticationType.DATABASE;
        }
    }
}
