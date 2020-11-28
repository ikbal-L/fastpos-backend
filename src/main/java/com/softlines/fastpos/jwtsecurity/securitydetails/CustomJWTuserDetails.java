package com.softlines.fastpos.jwtsecurity.securitydetails;

import com.softlines.fastpos.jwtsecurity.securitydomain.Agent;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomJWTuserDetails implements UserDetails {

    private final JWTuser jwtUser;
    private Collection<GrantedAuthority> grantedAuthorities;


    public CustomJWTuserDetails(JWTuser jwtUser, Collection<GrantedAuthority> grantedAuthorities) {
        this.jwtUser = jwtUser;
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return jwtUser.getPassword();
    }

    @Override
    public String getUsername() {
        return jwtUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return jwtUser.isEnabled();
    }


    public JWTuser getJwtUser() {
        return jwtUser;
    }
}
