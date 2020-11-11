package com.softlines.fastpos.jwtsecurity.securityfilters;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softlines.fastpos.jwtsecurity.securitydetails.CustomJWTuserDetails;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.UserDTO;
import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.EXPIRATION_TIME;
import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.SECRET;
import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.HEADER_STRING;
import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private UserDTO creds;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            creds = new ObjectMapper()
                    .readValue(req.getInputStream(), UserDTO.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            Arrays.asList())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) {
        //TODO when changing dbInfo by dbId, you should change this instruction : DONE!
        var dbId = ((CustomJWTuserDetails) auth.getPrincipal()).getDbId() == null ? 0
                : ((CustomJWTuserDetails) auth.getPrincipal()).getDbId();

        String token = createToken(auth.getName(), dbId);
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }

    private String createToken(String name, long dbID){
        String token = JWT.create()
                .withSubject(name)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim("dbID", dbID)
                .withClaim("annexId", creds.getAnnexId())
                .withClaim("terminalId", creds.getTerminalId())
                .sign(HMAC512(SECRET.getBytes()));
        return token;
    }


}