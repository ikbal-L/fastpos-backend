package com.softlines.fastpos.jwtsecurity.securityservice;

import com.softlines.fastpos.jwtsecurity.securitydetails.CustomJWTuserDetails;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JWTuserDetailsServiceImpl implements UserDetailsService {

    private JWTuserRepository jwTuserRepository;

    public JWTuserDetailsServiceImpl(JWTuserRepository jwTuserRepository) {
        this.jwTuserRepository = jwTuserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JWTuser jwTuser = jwTuserRepository.findByUsername(username);
        if (jwTuser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomJWTuserDetails(jwTuser);
    }
}
