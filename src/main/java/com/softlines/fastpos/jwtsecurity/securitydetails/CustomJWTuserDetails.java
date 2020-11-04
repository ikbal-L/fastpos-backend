package com.softlines.fastpos.jwtsecurity.securitydetails;

import com.softlines.fastpos.jwtsecurity.securitydomain.DbInfo;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomJWTuserDetails implements UserDetails {

    private final JWTuser jwTuser;

    public CustomJWTuserDetails(JWTuser jwTuser) {
        this.jwTuser = jwTuser;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role role:
             jwTuser.getRoles()) {
            for (Privilege privilege:
                 role.getPrivileges()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(privilege.getName()));
            }
        }
        return grantedAuthorities;
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
