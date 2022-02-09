package com.softlines.fastpos.security.securityconfiguration;

import com.softlines.fastpos.security.securityfilters.ApiAuthorizationFilter;
import com.softlines.fastpos.security.securityfilters.AuthenticationFilter;
import com.softlines.fastpos.security.securityfilters.ConfigAuthorizationFilter;
import com.softlines.fastpos.licensing.LicenseActivationFilter;
import com.softlines.fastpos.security.securityrepository.AnnexRepository;
import com.softlines.fastpos.security.securityrepository.SessionRepository;
import com.softlines.fastpos.security.securityservice.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.Filter;

import static com.softlines.fastpos.security.securityfilters.SecurityConstants.SIGN_UP_URL;

@EnableGlobalMethodSecurity(prePostEnabled = true)

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Order(1)
    @Configuration
    public static class SecurityConfiguration1 extends WebSecurityConfigurerAdapter {
//
//        @Qualifier("UserDetailsServiceImpl")
        @Autowired
        private UserDetailsServiceImpl userDetailsService;
        @Autowired
        private SessionRepository sessionRepository;
        @Autowired
        private AuthenticationFilter authenticationFilter;
        @Autowired
        private LicenseActivationFilter licenseActivationFilter;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
        }

        @Bean
        public AuthenticationManager getAuthenticationManager() throws Exception {
            return authenticationManager();
        }

        private PasswordEncoder getPasswordEncoder() {
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                    .formLogin()
                    .successHandler(authSuccessHandler())
                    .failureHandler(authenticationFailureHandler()).and()

                    .antMatcher("/api/**")
//                    .addFilterBefore(licenseActivationFilter,AuthenticationFilter.class)
                    .cors().and().csrf().disable().authorizeRequests()
                    .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                    .antMatchers(HttpMethod.POST, "/login").permitAll()

                    .anyRequest()
//                .permitAll()
                    .authenticated()
                    .and().formLogin().failureHandler(authenticationFailureHandler())
                    .and()
                    .addFilter(authenticationFilter)
                    .addFilter(new ApiAuthorizationFilter(authenticationManager(), sessionRepository))

                    // this disables session creation on Spring Security
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        }



    }
    @Bean(name = "apiAuth")
    ApiSecurity webSecurity() {
        return new ApiSecurity();
    }
    @Bean
    public static AuthenticationFailureHandler authenticationFailureHandler() {
        return new ApiAuthenticationFailureHandler();
    }
    @Bean
    public static AuthenticationSuccessHandler authSuccessHandler() {
        return new ApiAuthenticationSuccessHandler();
    }


    @Order(2)
    @Configuration
    public static class SecurityConfiguration2 extends WebSecurityConfigurerAdapter {

//        @Qualifier("UserDetailsServiceImpl")
        @Autowired
        private UserDetailsServiceImpl userDetailsService;
        @Autowired
        private AnnexRepository annexRepository;
        @Autowired
        private AuthenticationFilter authenticationFilter;
        @Autowired
        private LicenseActivationFilter licenseActivationFilter;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
        }

        @Bean
        public AuthenticationManager getAuthenticationManager() throws Exception {
            return authenticationManager();
        }

        private PasswordEncoder getPasswordEncoder() {
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                    .antMatcher("/config/**")
//                    .addFilterBefore(licenseActivationFilter,AuthenticationFilter.class)
                    .cors().and().csrf().disable().authorizeRequests()
                    //.antMatchers(HttpMethod.POST, "/user/save").permitAll()
                    .anyRequest()
//                .permitAll()
                    .authenticated()
                    .and()
                    .addFilter(authenticationFilter)
                    .addFilter(new ConfigAuthorizationFilter(authenticationManager(), annexRepository))
                    // this disables session creation on Spring Security
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }

    }
}