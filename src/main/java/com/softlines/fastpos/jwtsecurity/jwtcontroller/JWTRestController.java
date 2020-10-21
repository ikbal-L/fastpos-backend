package com.softlines.fastpos.jwtsecurity.jwtcontroller;


import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
