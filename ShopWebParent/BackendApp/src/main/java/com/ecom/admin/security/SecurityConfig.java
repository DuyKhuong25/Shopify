package com.ecom.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration

public class SecurityConfig{

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests(configuer ->
                configuer.anyRequest().authenticated()
        ).formLogin(form ->
                form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .loginProcessingUrl("/processLogin")
                        .permitAll()
        ).logout(logout -> logout.permitAll()
        ).rememberMe(rem ->
                rem
                        .userDetailsService(userDetailsService())
                        .key("AbcDefghiklmnoputrq_123456789")
                        .tokenValiditySeconds(3*24*60*60)
        );
        return http.build();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/css/**");
    }
}
