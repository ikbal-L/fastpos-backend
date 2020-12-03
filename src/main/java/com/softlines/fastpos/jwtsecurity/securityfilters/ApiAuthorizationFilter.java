package com.softlines.fastpos.jwtsecurity.securityfilters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.softlines.fastpos.dbconfig.configuration.CustomContextHolder;
import com.softlines.fastpos.jwtsecurity.securityconfiguration.FilterResponseWrapper;
import com.softlines.fastpos.jwtsecurity.securitydomain.Annex;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securityrepository.AnnexRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.SessionRepository;
import org.apache.catalina.connector.Response;
import org.apache.catalina.connector.ResponseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.HeaderWriterFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.SECRET;
import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.HEADER_STRING;
import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.TOKEN_PREFIX;

public class ApiAuthorizationFilter extends BasicAuthenticationFilter {

    private SessionRepository sessionRepository;

    public ApiAuthorizationFilter(AuthenticationManager authManager, SessionRepository sessionRepository) {
        super(authManager);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {

//            chain.doFilter(req, res);
            res.setStatus(HttpStatus.BAD_REQUEST.value());
            res.getOutputStream().print("Token format exception");
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
//        UsernamePasswordAuthenticationToken authentication = null;
        if (authentication==null){
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.getOutputStream().print("Something happened");
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        res.setStatus(HttpStatus.NO_CONTENT.value());
        chain.doFilter(req, res);

    }



    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);


        if (token != null) {
            // parse the token.
            DecodedJWT decoded = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""));
            String user = decoded.getSubject();

            if (user != null) {
//                Claim dbID = decoded.getClaim("dbID");
//                Claim annexId = decoded.getClaim("annexId");
                Claim sessionId = decoded.getClaim("sessionId");
                UUID sessionUUID  = sessionId.as(UUID.class);
//                var sessionOptional = sessionRepository.findById(sessionId.as(UUID.class));
                var sessionOptional = sessionRepository.findById(sessionUUID);
                if (sessionOptional.isPresent()){
                    var terminal = sessionOptional.get().getTerminal();
                    var annex = terminal.getAnnex();
                    var dbinfo = annex.getDbInfo();

                    Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                    for (String authority: decoded.getClaim("grantedAuthorities").asList(String.class)) {
                        grantedAuthorities.add(new SimpleGrantedAuthority(authority));
                    }
                    if (dbinfo!=null){
                        CustomContextHolder.clear();
                        CustomContextHolder.setId(dbinfo.getId());
                        return new UsernamePasswordAuthenticationToken(sessionId.asString(), null, grantedAuthorities);
                    }
                    return null;
                }
//                Map<String, Long> claims = new HashMap<>();
//                claims.put("dbID", dbID.asLong());
//                claims.put("annexId", annexId.asLong());
//                claims.put("terminalId", terminalId.asLong());
//

                return null;
            }
            return null;
        }
        return null;
    }
}