package com.softlines.fastpos.licensing;

import com.softlines.models.User;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

@Getter
@Setter


public class LicenseState {

    private User credentials;
    private boolean initialized = false;
    private boolean licenseActivationRequested = false;
    private boolean serialKeyValid = false;
    private static LicenseState licenseState = new LicenseState();

    private LicenseState(User credentials) {
        this.credentials = credentials;
    }

    private LicenseState() {

    }

    @NotNull
    public static LicenseState getInstance(){
        return licenseState;
    }
    public LicenseState getInstanceWith(User credentials){
        licenseState.setCredentials(credentials);
        return licenseState;
    }

    public boolean isOk(){
        return initialized&& licenseActivationRequested && serialKeyValid;
    }
}
