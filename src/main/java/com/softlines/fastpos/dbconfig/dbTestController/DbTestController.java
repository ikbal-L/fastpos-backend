package com.softlines.fastpos.dbconfig.dbTestController;

import com.softlines.fastpos.jwtsecurity.securityrepository.PrivilegeRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.RoleRepository;
import com.softlines.fastpos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dbtest")
public class DbTestController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PrivilegeRepository privilegeRepository;

    @PreAuthorize("@apiAuth.checkGrants(authentication, 'READ_PRIVILEGE')")
    @GetMapping(value = "/", produces = "application/json")
    public String testDbRerouting(){
        String name = productRepository.findAll().get(0).getName();
        return name;
    }

}
