package com.softlines.fastpos.security.securityfilters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.softlines.fastpos.security.securityrepository.AnnexRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.softlines.fastpos.security.securityfilters.SecurityConstants.*;

public class ConfigAuthorizationFilter extends BasicAuthenticationFilter {

    private AnnexRepository annexRepository;
    public ConfigAuthorizationFilter(AuthenticationManager authManager, AnnexRepository annexRepository) {
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
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}