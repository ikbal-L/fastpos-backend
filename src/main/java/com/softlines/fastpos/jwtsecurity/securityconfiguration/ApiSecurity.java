package com.softlines.fastpos.jwtsecurity.securityconfiguration;

import com.softlines.fastpos.dbconfig.configuration.CustomContextHolder;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
import org.apache.catalina.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

import java.util.ArrayList;

public class ApiSecurity {

    @Autowired
    JWTuserRepository jwTuserRepository;

    ArrayList<GrantedAuthority> grantedAuthorities;

    public  boolean checkGrants(Authentication auth, String privilege){

        grantedAuthorities = new ArrayList<>();
        JWTuser jwTuser = jwTuserRepository.findByUsername(auth.getName());
        for (Role role:
                jwTuser.getRoles()) {
            for (Privilege privilege1:
                    role.getPrivileges()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(privilege1.getName()));
            }
        }

        Assert.notNull(auth, "Authentication is null");
        Assert.notNull(auth, "privilege is null");
        Assert.isTrue(auth.isAuthenticated(), "User Not Authenticated");
        CustomContextHolder.clear();
        CustomContextHolder.setId((Long)auth.getCredentials());

        //return true;
        return grantedAuthorities.contains(new SimpleGrantedAuthority(privilege));
    }

    LocalContainerEntityManagerFactoryBean bb(){
        return null;
    }
}