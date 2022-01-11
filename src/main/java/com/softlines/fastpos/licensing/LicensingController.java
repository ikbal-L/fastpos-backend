package com.softlines.fastpos.licensing;

import com.softlines.licensing.client.services.LicenseActivationService;
import com.softlines.models.Result;
import com.softlines.models.ResultImpl;
import com.softlines.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping(value = "/licensing")
public class LicensingController {
    private  LicenseState licenseState ;
    private LicenseActivationService licenseActivationService;

    @Value("${com.softlines.product.id}")
    private Long productId;



    public LicensingController(@Value("${com.softlines.licensing.server}") String licensingServerUrl) {
        licenseState = LicenseState.getInstance();
        licenseActivationService= new LicenseActivationService(licensingServerUrl);
    }
    @GetMapping("/get-state")
    public ResponseEntity<?> getState() throws IOException {
        var isServerActivationRequired = licenseActivationService.isServerActivationRequired(productId);
        if (isServerActivationRequired){
           return ResponseEntity.status(455).body(List.of("com.softlines.errors.licensing.ServerActivationRequired"));
        }

        var serialKeyVerificationResult = doSerialKeyVerification();
        if (serialKeyVerificationResult.isSuccessful()&&serialKeyVerificationResult.get()){

            licenseState.setInitialized(true);
            licenseState.setLicenseActivationRequested(true);
            licenseState.setSerialKeyValid(true);

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(455).body(serialKeyVerificationResult.getErrors());

    }

    @PostMapping("/server-activation")
    public ResponseEntity<?> doServerActivation(@RequestBody User credentials) throws IOException {
        licenseState.setCredentials(credentials);
        var serverActivationResult = doRequestServerActivation(licenseState);
        if (serverActivationResult.isSuccessful()&& serverActivationResult.get()){

            licenseState.setInitialized(true);
            licenseState.setLicenseActivationRequested(true);
            licenseState.setSerialKeyValid(true);

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(455).body(serverActivationResult.getErrors());
    }

    private Result<Boolean> doSerialKeyVerification() throws IOException {
        return licenseActivationService.validateSerialKey(productId);
    }

    private Result<Boolean> doRequestServerActivation(LicenseState licenseState) throws IOException {

        var authResult = licenseActivationService.authenticate(licenseState.getCredentials());
        if (authResult.isSuccessful())
        if (authResult.isSuccessful() && authResult.get()){
            var activationResult = licenseActivationService.activateFromServer(productId);

            if (activationResult.isSuccessful()&& activationResult.get()){
                return new ResultImpl<>(true);
            }
            // if requesting activation fails
            return activationResult;
        }
        // if authentication fails
        return authResult;
    }



}
