package com.softlines.fastpos.security.securityconfiguration;

import com.softlines.fastpos.security.securityservice.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class SecurityBeans {
    @Bean("authMan")
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(List.of(daoAuthenticationProvider));
    }

//    @Bean("userDetailsService")
//    public UserDetailsService userDetailsService(UserDetailsServiceImpl userDetailsServiceImpl) {
//        return userDetailsServiceImpl;
//    }


}
