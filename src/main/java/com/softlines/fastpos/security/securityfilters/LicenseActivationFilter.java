package com.softlines.fastpos.security.securityfilters;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.softlines.licensing.client.services.LicenseActivationService;
import com.softlines.licensing.commons.errors.LicensingErrors;
import com.softlines.models.Result;
import com.softlines.models.User;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;

@Component
@Order(1)
public class LicenseActivationFilter implements Filter {

    private boolean initialized = false;
    private boolean isLicenseActivationRequested = false;
    private boolean isSerialKeyValid = false;
    private final LicenseActivationService licenseActivationService;
    @Value("${com.softlines.product.id}")
    private Long productId;

    public LicenseActivationFilter(@Value("${com.softlines.licensing.server}") String licensingServerUrl) {
        licenseActivationService= new LicenseActivationService(licensingServerUrl);
    }



    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        if (!initialized){
            if (licenseActivationService.isServerActivationRequired(productId)){
                doRequestServerActivation(request, response);
            }else {
                doSerialKeyVerification(response);
            }
        }

        if (initialized&& isLicenseActivationRequested && isSerialKeyValid){
            chain.doFilter(request,response);
        }
    }

    private void doSerialKeyVerification(ServletResponse response) throws IOException {
        var validationResult=  licenseActivationService.validateSerialKey(productId);

        initialized = true;
        isLicenseActivationRequested = true;
        if (!validationResult.isSuccessful()){
            writeErrorsToResponse(response,validationResult.getErrors());
        }

        isSerialKeyValid = validationResult.get();
    }

    @Nullable
    private User getCredentials(ServletRequest request, ServletResponse response) throws IOException {
        User credentials;
        HttpServletRequest req = (HttpServletRequest)request;
        var credentialsHeader =  req.getHeader("Licensing-Credentials");
        if (credentialsHeader== null||credentialsHeader.isBlank()){
            writeErrorsToResponse(response, LicensingErrors.CREDENTIALS_MISSING);
            return null;
        }
        try {
            credentials = new ObjectMapper().readValue(credentialsHeader, User.class);
        } catch (IOException e) {
            writeErrorsToResponse(response,LicensingErrors.CREDENTIALS_FORMAT);
            return null;
        }
        return credentials;
    }

    private void doRequestServerActivation(ServletRequest request,ServletResponse response) throws IOException {

        User credentials = getCredentials(request, response);
        if (credentials == null) return;

        Result<Boolean> activationResult;
        var authResult = licenseActivationService.authenticate(credentials);

        if (authResult.isSuccessful()&& authResult.get()){
            activationResult = licenseActivationService.activateFromServer(productId);

            if (activationResult.isSuccessful()&& activationResult.get()){
                initialized = true;
                isLicenseActivationRequested = true;
                isSerialKeyValid = true;
                return;
            }
            // if requesting activation fails
            writeErrorsToResponse(response, activationResult.getErrors());
            return;
        }
        // if authentication fails
        writeErrorsToResponse(response, authResult.getErrors());
    }

    private void writeErrorsToResponse(ServletResponse response, String... errors) throws IOException {
        writeErrorsToResponse(response, Arrays.asList(errors));
    }

    private void writeErrorsToResponse(ServletResponse response, Collection<String> errors) throws IOException {
        var content = new ObjectMapper().writeValueAsString(errors);
        PrintWriter writer = response.getWriter();
        HttpServletResponse res = (HttpServletResponse) response;
        res.setStatus(445);
        writer.print(content);
        writer.flush();
    }

}
