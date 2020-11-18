package com.softlines.fastpos.jwtsecurity.securityfilters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.softlines.fastpos.dbconfig.configuration.CustomContextHolder;
import com.softlines.fastpos.jwtsecurity.securitydomain.Annex;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securityrepository.AnnexRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
import org.apache.catalina.connector.Response;
import org.apache.catalina.connector.ResponseFacade;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.SECRET;
import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.HEADER_STRING;
import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.TOKEN_PREFIX;

public class ApiAuthorizationFilter extends BasicAuthenticationFilter {

    private AnnexRepository annexRepository;
    public ApiAuthorizationFilter(AuthenticationManager authManager, AnnexRepository annexRepository) {
        super(authManager);
        this.annexRepository = annexRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }


    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);

        String annexIdString = request.getHeader("Annex-Id");
        if (annexIdString==null ) return null;
        long annexId = -1;
        try {
            annexId = Long.parseLong(annexIdString);
        } catch (NumberFormatException e) {

            return null;
        }

        Annex annex  = null;
        var annexOptional = annexRepository.findById(annexId);
        if (annexOptional.isPresent()) annex = annexOptional.get();

        if (token != null) {
            // parse the token.
            DecodedJWT decoded = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""));
            String user = decoded.getSubject();

            if (user != null) {
//                Claim dbID = decoded.getClaim("dbID");
//                Claim annexId = decoded.getClaim("annexId");
//                Claim terminalId = decoded.getClaim("terminalId");
//                Map<String, Long> claims = new HashMap<>();
//                claims.put("dbID", dbID.asLong());
//                claims.put("annexId", annexId.asLong());
//                claims.put("terminalId", terminalId.asLong());
//
                if (annex!=null){
                    var optionalUser = annex.getUsers().stream().takeWhile(u->u.getId()==decoded.getClaim("userId").asLong()).findFirst();
                    if (optionalUser.isEmpty())
                        return null;
                    CustomContextHolder.clear();
                    CustomContextHolder.setId(annex.getDbInfo().getId());
                    return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                }
                return null;
            }
            return null;
        }
        return null;
    }
}