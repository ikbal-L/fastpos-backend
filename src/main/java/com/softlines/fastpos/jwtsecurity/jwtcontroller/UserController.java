package com.softlines.fastpos.jwtsecurity.jwtcontroller;


import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.PrivilegeRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    JWTuserRepository jwTuserRepository;
    @Autowired
    PrivilegeRepository privilegeRepository;
    @Autowired
    RoleRepository roleRepository;

    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PutMapping("/addprivilege/{privilegeName}")
    public void addPrivilege(@PathVariable String privilegeName, HttpServletResponse response){

        if(!privilegeName.matches("_PRIVILEGE$")) privilegeName = privilegeName.toUpperCase() + "_PRIVILEGE";

        if(privilegeRepository.findByName(privilegeName) == null){
            Privilege privilege = new Privilege();
            privilege.setName(privilegeName);
            privilegeRepository.save(privilege);
            response.setStatus(HttpServletResponse.SC_CREATED);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping("/deleteprivilege/{privilegeName}")
    public void deletePrivilege(@PathVariable String privilegeName, HttpServletResponse response){

        if(!privilegeName.matches("_PRIVILEGE$")) privilegeName = privilegeName.toUpperCase() + "_PRIVILEGE";

        Privilege privilege = privilegeRepository.findByName(privilegeName);

        if(!(privilege == null)){
            privilegeRepository.delete(privilege);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PutMapping("/addrole/{roleName}")
    public void addRole(@PathVariable String roleName, HttpServletResponse response){

        if(!roleName.matches("^ROLE_")) roleName = "ROLE_" + roleName.toUpperCase();

        if(roleRepository.findByName(roleName) == null){
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
            response.setStatus(HttpServletResponse.SC_CREATED);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping("/deleterole/{roleName}")
    public void deleteRole(@PathVariable String roleName){

        if(!roleName.matches("^ROLE_")) roleName = "ROLE_" + roleName.toUpperCase();

        Role role = roleRepository.findByName(roleName);

        if(!(role == null)){
            roleRepository.delete(role);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PutMapping("/addprivilegetorole/{roleName}/{privilegeName}")
    public void addPrivilegeToRole(@PathVariable("roleName") String roleName, @PathVariable("privilegeName") String privilegeName, HttpServletResponse response){

        if(!roleName.matches("^ROLE_")) roleName = "ROLE_" + roleName.toUpperCase();
        if(!privilegeName.matches("_PRIVILEGE$")) privilegeName = privilegeName.toUpperCase() + "_PRIVILEGE";

        Role role = roleRepository.findByName(roleName);
        Privilege privilege = privilegeRepository.findByName(privilegeName);

        List<String> privileges = new ArrayList<>();

        for (Privilege privilege1:
             role.getPrivileges()) {
            privileges.add(privilege1.getName());
        }

        if(!(role == null) && !(privilege == null) && !(privileges.contains(privilegeName))){
            roleRepository.addPrivilege(role.getId(), privilege.getId());
        }else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping("/removeprivilegefromrole/{roleName}/{privilegeName}")
    public void removePrivilegeFromRole(@PathVariable("roleName") String roleName, @PathVariable("privilegeName") String privilegeName, HttpServletResponse response){

        if(!roleName.matches("^ROLE_")) roleName = "ROLE_" + roleName.toUpperCase();
        if(!privilegeName.matches("_PRIVILEGE$")) privilegeName = privilegeName.toUpperCase() + "_PRIVILEGE";

        Role role = roleRepository.findByName(roleName);
        Privilege privilege = privilegeRepository.findByName(privilegeName);

        List<String> privileges = new ArrayList<>();

        for (Privilege privilege1:
                role.getPrivileges()) {
            privileges.add(privilege1.getName());
        }

        if(!(role == null) && !(privilege == null) && (privileges.contains(privilegeName))){
            roleRepository.removePrivilege(role.getId(), privilege.getId());
        }else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PutMapping("/addroletouser/{userName}/{roleName}")
    public void addRoleToUser(@PathVariable("userName") String userName, @PathVariable("roleName") String roleName, HttpServletResponse response){

        if(!roleName.matches("^ROLE_")) roleName = "ROLE_" + roleName.toUpperCase();

        Role role = roleRepository.findByName(roleName);
        JWTuser jwTuser = jwTuserRepository.findByUsername(userName);

        List<String> roles = new ArrayList<>();

        for (Role role1:
                jwTuser.getRoles()) {
            roles.add(role1.getName());
        }

        if(!(jwTuser == null) && !(role == null) && !(roles.contains(roleName))){
            jwTuserRepository.addRole(jwTuser.getId(), role.getId());
        }else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping("/removerolefromuser/{userName}/{roleName}")
    public void removeRoleFromUser(@PathVariable("userName") String userName, @PathVariable("roleName") String roleName, HttpServletResponse response){

        if(!roleName.matches("^ROLE_")) roleName = "ROLE_" + roleName.toUpperCase();

        Role role = roleRepository.findByName(roleName);
        JWTuser jwTuser = jwTuserRepository.findByUsername(userName);

        List<String> roles = new ArrayList<>();

        for (Role role1:
                jwTuser.getRoles()) {
            roles.add(role1.getName());
        }

        if(!(jwTuser == null) && !(role == null) && (roles.contains(roleName))){
            jwTuserRepository.removeRole(jwTuser.getId(), role.getId());
        }else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
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
