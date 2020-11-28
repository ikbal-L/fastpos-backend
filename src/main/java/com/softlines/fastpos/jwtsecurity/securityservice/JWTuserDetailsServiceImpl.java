package com.softlines.fastpos.jwtsecurity.securityservice;

import com.softlines.fastpos.jwtsecurity.securitydetails.CustomJWTuserDetails;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.PrivilegeRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Service
public class JWTuserDetailsServiceImpl implements UserDetailsService {

    private JWTuserRepository jwTuserRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private RoleRepository roleRepository ;

    private Collection<GrantedAuthority> grantedAuthorities;


    public JWTuserDetailsServiceImpl(JWTuserRepository jwTuserRepository) {
        this.jwTuserRepository = jwTuserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JWTuser jwTuser = jwTuserRepository.findByUsername(username);
        if (jwTuser == null) {
            throw new UsernameNotFoundException(username);
        }

        grantedAuthorities = new ArrayList<>();

        Set<Privilege> privileges = privilegeRepository.getUserPrivileges(jwTuser.getId());

        Set<Role> roles = roleRepository.getUserRoles(jwTuser.getId());

        for (Privilege privilege: privileges) {
            grantedAuthorities.add(new SimpleGrantedAuthority(privilege.getName()));
        }

        for (Role role: roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new CustomJWTuserDetails(jwTuser,grantedAuthorities);
    }
}
