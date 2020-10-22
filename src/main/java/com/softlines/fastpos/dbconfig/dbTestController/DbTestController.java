package com.softlines.fastpos.dbconfig.dbTestController;

import com.softlines.fastpos.dbconfig.configuration.CustomContextHolder;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.PrivilegeRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.RoleRepository;
import com.softlines.fastpos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.List;

@RestController
@RequestMapping("/dbtest")
public class DbTestController {

//    @Autowired
//    @Qualifier("customRoutingDataSource")
//    DataSource dataSource;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PrivilegeRepository privilegeRepository;

    @PreAuthorize("@apiAuth.checkGrants(authentication, 'WRITE_PRIVILEGE')")
    @GetMapping(value = "/", produces = "application/json")
    public String testDbRerouting(){

        String name = productRepository.findAll().get(0).getName();
        return name;

    }

    @Autowired
    JWTuserRepository jwTuserRepository;

    @GetMapping("/users")
    public List<JWTuser> getAllUsers() {
        return jwTuserRepository.findAll();
    }

    @GetMapping("/getbyID/{id}")
    public JWTuser getAllUsers(@PathVariable long id) {
        JWTuser jwTuser = jwTuserRepository.findById(id).get();
        return jwTuser;
    }

    @GetMapping("/getRoleByID/{id}")
    public Role role(@PathVariable long id){
        return roleRepository.findById(id).get();
    }

}
