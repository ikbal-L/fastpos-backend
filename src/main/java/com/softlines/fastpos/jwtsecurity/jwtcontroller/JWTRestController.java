package com.softlines.fastpos.jwtsecurity.jwtcontroller;


import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class JWTRestController {

    @Autowired
    JWTuserRepository jwTuserRepository;

    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @GetMapping("/testjwt")
    public String testJWTSecurity(){
        return "DONE!";
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping("/delete/{username}")
    public void deleteByUserame(@PathVariable String username){
        JWTuser jwTuser = jwTuserRepository.findByUsername(username);
        jwTuserRepository.delete(jwTuser);
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody JWTuser jwTuser){

        jwTuser.setPassword(encoder.encode(jwTuser.getPassword()));

        jwTuserRepository.save(jwTuser);
    }

    @RequestMapping("/getbyID/{id}")
    public JWTuser getById(@PathVariable long id){
        return jwTuserRepository.findById(id).get();
    }



}
