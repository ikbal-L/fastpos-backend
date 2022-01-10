package com.softlines.fastpos.security.provider;

import com.softlines.fastpos.security.exceptions.AlreadySignedInException;
import com.softlines.fastpos.security.securitydetails.CustomUserDetails;
import com.softlines.fastpos.security.securityservice.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DaoAuthenticationProviderEx extends DaoAuthenticationProvider {

    public DaoAuthenticationProviderEx(UserDetailsServiceImpl userDetailsService) {
        this.setUserDetailsService(userDetailsService);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        if (userDetails instanceof CustomUserDetails){
            var user = ((CustomUserDetails)userDetails).getUser();
            if (user!= null&& user.isSignedIn()){
                throw new AlreadySignedInException("Authentication failed due to user being already signed-in");
            }
        }

        super.additionalAuthenticationChecks(userDetails,authentication);
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        ((CustomUserDetails)user).getUser().setSignedIn(true);
        return super.createSuccessAuthentication(principal, authentication, user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
