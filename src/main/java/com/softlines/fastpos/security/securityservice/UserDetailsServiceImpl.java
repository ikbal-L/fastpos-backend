package com.softlines.fastpos.security.securityservice;

import com.softlines.fastpos.security.securitydetails.CustomUserDetails;
import com.softlines.fastpos.security.securitydomain.User;
import com.softlines.fastpos.security.securitydomain.Privilege;
import com.softlines.fastpos.security.securitydomain.Role;
import com.softlines.fastpos.security.securityrepository.UserRepository;
import com.softlines.fastpos.security.securityrepository.PrivilegeRepository;
import com.softlines.fastpos.security.securityrepository.RoleRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;


    private final PrivilegeRepository privilegeRepository;


    private final RoleRepository roleRepository ;

    private Collection<GrantedAuthority> grantedAuthorities;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        grantedAuthorities = new ArrayList<>();

        Set<Privilege> privileges = privilegeRepository.getUserPrivileges(user.getId());

        Set<Role> roles = roleRepository.getUserRoles(user.getId());

        for (Privilege privilege: privileges) {
            grantedAuthorities.add(new SimpleGrantedAuthority(privilege.getName()));
        }

        for (Role role: roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new CustomUserDetails(user,grantedAuthorities);
    }
}
