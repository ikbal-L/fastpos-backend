package com.softlines.fastpos.jwtsecurity.securitydetails;

import com.softlines.fastpos.jwtsecurity.securitydomain.DbInfo;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securityrepository.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomJWTuserDetails implements UserDetails {

    private final JWTuser jwTuser;

    public CustomJWTuserDetails(JWTuser jwTuser) {
        this.jwTuser = jwTuser;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return jwTuser.getPassword();
    }

    @Override
    public String getUsername() {
        return jwTuser.getUsername();
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
        return jwTuser.isEnabled();
    }

    public Long getDbId() {
        return jwTuser.getDbId();
    }
}
