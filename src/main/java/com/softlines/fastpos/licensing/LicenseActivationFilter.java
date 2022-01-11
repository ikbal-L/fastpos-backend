package com.softlines.fastpos.licensing;


import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.inject.Singleton;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Singleton
@Component
@Order(1)
public class LicenseActivationFilter extends OncePerRequestFilter {

    private final LicenseState licenseState ;

    public LicenseActivationFilter() {
        licenseState = LicenseState.getInstance();
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        if (!licenseState.isOk()) {
            request = new HttpServletRequestWrapper(request) {
                @Override
                public String getRequestURI() {
                    return "/licensing/get-state";
                }

            };

        }
        filterChain.doFilter(request,response);


    }

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        return request.getRequestURI().startsWith("/licensing");
//    }
}
