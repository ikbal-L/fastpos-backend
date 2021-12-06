package com.softlines.fastpos.security.securityconfiguration;

import com.softlines.fastpos.security.securitydomain.User;
import com.softlines.fastpos.security.securitydomain.Privilege;
import com.softlines.fastpos.security.securitydomain.Role;
import com.softlines.fastpos.security.securitydomain.Session;
import com.softlines.fastpos.security.securityrepository.UserRepository;
import com.softlines.fastpos.security.securityrepository.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class ApiSecurity {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PrivilegeRepository privilegeRepository;

    List<GrantedAuthority> grantedAuthorities;

    ArrayList<String> roles;

    public  boolean checkGrants(Authentication auth, String privilege){

        Assert.notNull(auth, "Authentication is null");
        Assert.notNull(auth, "privilege is null");
        Assert.isTrue(auth.isAuthenticated(), "User Not Authenticated");
        var session = (Session) auth.getPrincipal();
        var user = session.getUser();
        var privileges = privilegeRepository.findUserPrivileges(user.getUsername());
        grantedAuthorities = new ArrayList<>();
        for (Privilege privilege1: privileges) {
            grantedAuthorities.add(new SimpleGrantedAuthority(privilege1.getName()));

        }
        //TODO Where to switch DB?
//        CustomContextHolder.clear();
//        CustomContextHolder.setId(((HashMap<String, Long>)auth.getCredentials()).get("dbID"));

        return grantedAuthorities.contains(new SimpleGrantedAuthority(privilege));
    }

    @Transactional("authTransactionManager")
    public boolean checkRoles(Authentication auth, String role) {

        Assert.notNull(auth, "Authentication is null");
        Assert.notNull(auth, "privilege is null");
        Assert.isTrue(auth.isAuthenticated(), "User Not Authenticated");

        roles = new ArrayList<>();
        User user = userRepository.findByUsername(auth.getName());
        for (Role userRole : user.getRoles()) {
            roles.add(userRole.getName());
        }
        return roles.contains(role);
    }
}