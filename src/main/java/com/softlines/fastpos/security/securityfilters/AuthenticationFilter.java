package com.softlines.fastpos.security.securityfilters;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softlines.fastpos.security.securitydetails.CustomUserDetails;
import com.softlines.fastpos.security.securitydomain.Session;
import com.softlines.fastpos.security.securitydomain.securitydto.UserDTO;
import com.softlines.fastpos.security.securityrepository.SessionRepository;
import com.softlines.fastpos.security.securityrepository.TerminalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import static com.softlines.fastpos.security.securityfilters.SecurityConstants.EXPIRATION_TIME;
import static com.softlines.fastpos.security.securityfilters.SecurityConstants.SECRET;
import static com.softlines.fastpos.security.securityfilters.SecurityConstants.HEADER_STRING;
import static com.softlines.fastpos.security.securityfilters.SecurityConstants.TOKEN_PREFIX;
@Component
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    //TODO find a method to retrieve content of request after successful attempt [Remove unnecessary creds variable]
    private UserDTO creds;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private TerminalRepository terminalRepository ;



    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            creds = new ObjectMapper()
                    .readValue(req.getInputStream(), UserDTO.class);
            var auth = new UsernamePasswordAuthenticationToken(
                    creds.getUsername(),
                    creds.getPassword(),
                    Arrays.asList());

            return getAuthenticationManager().authenticate(auth);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {


        var user = ((CustomUserDetails) auth.getPrincipal()).getUser();
        var optionalTerminal = terminalRepository.findById(creds.getTerminalId());
        if (optionalTerminal.isPresent()){
            var terminal  = optionalTerminal.get();
            var dbId =terminal.getAnnex().getDbInfo().getId();
            Session session = Session.builder()
                    .date(new Date())
                    .user(user)
                    .terminal(terminal)
                    .agent(creds.getAgent())
                    .ipAddress(req.getRemoteAddr()).build();
            Session createdSession = null;

            try {
                createdSession = sessionRepository.save(session);
            } catch (DataIntegrityViolationException e) {
                res.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
            }

            String token = createToken(auth.getName(), user.getId(),createdSession,auth);
            res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

            List<String> grantedAuthorities = new ArrayList<>();
            for (GrantedAuthority grantedAuthority : auth.getAuthorities()) {
                grantedAuthorities.add(grantedAuthority.getAuthority());
            }

            var content = new ObjectMapper().writeValueAsString(grantedAuthorities);

//            res.resetBuffer();
//            res.setStatus(HttpStatus.OK.value());
//            res.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
//            res.getOutputStream().print(content);
//            res.flushBuffer();
            PrintWriter writer = res.getWriter();
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.setCharacterEncoding("UTF-8");
            res.addHeader("user-meta-background",user.getBackgroundString());
            res.addHeader("user-meta-session-id",session.getId().toString());
            writer.print(content);
            writer.flush();
        }


    }

    private String createToken(String name, long userId,Session session,Authentication auth){

        List<String> grantedAuthorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : auth.getAuthorities()) {
            grantedAuthorities.add(grantedAuthority.getAuthority());
        }

        String token = JWT.create()
                .withSubject(name)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim("sessionId", session==null?"":session.getId().toString())
                .withClaim("grantedAuthorities", grantedAuthorities)
//                .withClaim("userId",userId)
                .sign(HMAC512(SECRET.getBytes()));
        return token;
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
}