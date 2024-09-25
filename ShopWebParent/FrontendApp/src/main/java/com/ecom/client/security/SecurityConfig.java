package com.ecom.client.security;

import com.ecom.client.security.oauth2.CustomerOAuth2UserService;
import com.ecom.client.security.oauth2.OAuth2LoginSuccessHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Autowired
    @Lazy
    CustomerOAuth2UserService oAuth2UserService;

    @Autowired
    @Lazy
    OAuth2LoginSuccessHandle auth2LoginSuccessHandle;

    @Bean
    public UserDetailService userDetailsService(){
        return new UserDetailService();
    };

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
                configuer
                        .requestMatchers("/**").permitAll()
                        .anyRequest().permitAll()
        ).formLogin(form ->
                form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .loginProcessingUrl("/process_login")
                        .permitAll()
        ).oauth2Login(oauth2 ->
                oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(user -> user.userService(oAuth2UserService))
                        .successHandler(auth2LoginSuccessHandle)
        ).logout(logout -> logout.permitAll().logoutSuccessUrl("/")
        ).rememberMe(rem ->
                rem
                        .userDetailsService(userDetailsService())
                        .key("AbcDefghiklmnoputrq_123456789")
                        .tokenValiditySeconds(7*24*60*60)
        ).sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
        ).cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:80"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/css/**");
    }
}
