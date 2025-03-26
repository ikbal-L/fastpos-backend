package com.softlines.fastpos.security.securityconfiguration;

import com.softlines.fastpos.security.securityfilters.ApiAuthorizationFilter;
import com.softlines.fastpos.security.securityfilters.AuthenticationFilter;
import com.softlines.fastpos.security.securityfilters.ConfigAuthorizationFilter;
//import com.softlines.fastpos.licensing.LicenseActivationFilter;
import com.softlines.fastpos.security.securityrepository.AnnexRepository;
import com.softlines.fastpos.security.securityrepository.SessionRepository;
import com.softlines.fastpos.security.securityservice.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.Filter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

import static com.softlines.fastpos.security.securityfilters.SecurityConstants.SIGN_UP_URL;

@EnableGlobalMethodSecurity(prePostEnabled = true)

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Order(1)
    @Configuration
    public static class SecurityConfiguration1  {
//
//        @Qualifier("UserDetailsServiceImpl")
        @Autowired
        private UserDetailsServiceImpl userDetailsService;
        @Autowired
        private SessionRepository sessionRepository;
        @Autowired
        private AuthenticationFilter authenticationFilter;
//        @Autowired
//        private LicenseActivationFilter licenseActivationFilter;

//        @Override
//        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//            auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
//        }

        @Bean
        public static UserDetailsService userDetailsService(PasswordEncoder bCryptPasswordEncoder) {
            InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
            manager.createUser(User.withUsername("user")
                    .password(bCryptPasswordEncoder.encode("userPass"))
                    .roles("USER")
                    .build());
            manager.createUser(User.withUsername("admin")
                    .password(bCryptPasswordEncoder.encode("adminPass"))
                    .roles("USER", "ADMIN")
                    .build());
            return manager;
        }
//
//        @Bean
//        public AuthenticationManager getAuthenticationManager() throws Exception {
//            return authenticationManager();
//        }


        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder bCryptPasswordEncoder)
                throws Exception {
            return new ProviderManager(
                    List.of(new DaoAuthenticationProvider() {{
                        setUserDetailsService(userDetailsService);
                        setPasswordEncoder(bCryptPasswordEncoder);
                    }})
            );
        }

        @Bean
        protected PasswordEncoder getPasswordEncoder() {
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }


//        protected void configure(HttpSecurity http) throws Exception {
//
//            http
//                    .formLogin()
//                    .successHandler(authSuccessHandler())
//                    .failureHandler(authenticationFailureHandler()).and()
//
//                    .antMatcher("/api/**")
////                    .addFilterBefore(licenseActivationFilter,AuthenticationFilter.class)
//                    .cors().and().csrf().disable().authorizeRequests()
//                    .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
//                    .antMatchers(HttpMethod.POST, "/login").permitAll()
//
//                    .anyRequest()
////                .permitAll()
//                    .authenticated()
//                    .and().formLogin().failureHandler(authenticationFailureHandler())
//                    .and()
//                    .addFilter(authenticationFilter)
//                    .addFilter(new ApiAuthorizationFilter(authenticationManager(), sessionRepository))
//
//                    // this disables session creation on Spring Security
//                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        }
//
//
//
//    }

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .cors(Customizer.withDefaults()) // Enable CORS
            .csrf(csrf -> csrf.disable()) // Disable CSRF if needed
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.POST, "/signup", "/login").permitAll()
                    .requestMatchers("/api/**").authenticated()
                    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                    .successHandler(authSuccessHandler())
                    .failureHandler(authenticationFailureHandler())
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
         .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new ApiAuthorizationFilter(authenticationManager(http,getPasswordEncoder()), sessionRepository), UsernamePasswordAuthenticationFilter.class);

    return http.build();
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
    public static class SecurityConfiguration2   {

//        @Qualifier("UserDetailsServiceImpl")
        @Autowired
        private UserDetailsServiceImpl userDetailsService;
        @Autowired
        private AnnexRepository annexRepository;
        @Autowired
        private AuthenticationFilter authenticationFilter;
//        @Autowired
//        private LicenseActivationFilter licenseActivationFilter;
//
//        @Override
//        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//            auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
//        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder bCryptPasswordEncoder)
                throws Exception {
            return new ProviderManager(
                    List.of(new DaoAuthenticationProvider() {{
                        setUserDetailsService(userDetailsService);
                        setPasswordEncoder(bCryptPasswordEncoder);
                    }})
            );
        }



        private PasswordEncoder getPasswordEncoder() {
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }


        @Bean
        public SecurityFilterChain configSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                    .securityMatcher("/config/**") // Equivalent to `antMatcher`
                    .cors(Customizer.withDefaults()) // Enable CORS
                    .csrf(csrf -> csrf.disable()) // Disable CSRF if needed
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().authenticated()
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilter(authenticationFilter)
                    .addFilterBefore(new ConfigAuthorizationFilter(authenticationManager(http,getPasswordEncoder()),annexRepository), UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }

    }
}
}