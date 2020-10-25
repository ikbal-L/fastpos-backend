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
import java.util.Collection;
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
    public int addPrivilege(@PathVariable String privilegeName, HttpServletResponse response){

        privilegeName = privilegeName.toUpperCase();
        if(!privilegeName.matches("_PRIVILEGE$")) privilegeName = privilegeName.toUpperCase() + "_PRIVILEGE";

        if(privilegeRepository.findByName(privilegeName) == null){
            Privilege privilege = new Privilege();
            privilege.setName(privilegeName);
            privilegeRepository.save(privilege);
            return HttpServletResponse.SC_OK;
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return  HttpServletResponse.SC_BAD_REQUEST;
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping("/deleteprivilege/{privilegeName}")
    public int deletePrivilege(@PathVariable String privilegeName, HttpServletResponse response){

        privilegeName = privilegeName.toUpperCase();
        if(!privilegeName.matches("_PRIVILEGE$")) privilegeName = privilegeName.toUpperCase() + "_PRIVILEGE";

        Privilege privilege = privilegeRepository.findByName(privilegeName);

        if(!(privilege == null)){
            privilegeRepository.removeConstraint(privilege.getId());
            privilegeRepository.delete(privilege);
            return HttpServletResponse.SC_OK;
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return  HttpServletResponse.SC_BAD_REQUEST;
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PutMapping("/addrole/{roleName}")
    public int addRole(@PathVariable String roleName, HttpServletResponse response){

        roleName = roleName.toUpperCase();
        if(!roleName.matches("^ROLE_")) roleName = "ROLE_" + roleName.toUpperCase();

        if(roleRepository.findByName(roleName) == null){
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
            return HttpServletResponse.SC_OK;
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return  HttpServletResponse.SC_BAD_REQUEST;
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping("/deleterole/{roleName}")
    public int deleteRole(@PathVariable String roleName, HttpServletResponse response){

        roleName = roleName.toUpperCase();
        if(!roleName.matches("^ROLE_")) roleName = "ROLE_" + roleName.toUpperCase();

        Role role = roleRepository.findByName(roleName);

        if(!(role == null)){
            roleRepository.removePrivilegeConstraint(role.getId());
            roleRepository.removeUserConstraint(role.getId());
            roleRepository.delete(role);
            return HttpServletResponse.SC_OK;
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return  HttpServletResponse.SC_BAD_REQUEST;
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PutMapping("/addprivilegetorole/{roleName}/{privilegeName}")
    public int addPrivilegeToRole(@PathVariable("roleName") String roleName, @PathVariable("privilegeName") String privilegeName, HttpServletResponse response){

        roleName = roleName.toUpperCase();
        privilegeName = privilegeName.toUpperCase();
        if(!roleName.matches("^ROLE_")) roleName = "ROLE_" + roleName.toUpperCase();
        if(!privilegeName.matches("_PRIVILEGE$")) privilegeName = privilegeName.toUpperCase() + "_PRIVILEGE";

        Role role = roleRepository.findByName(roleName);
        Privilege privilege = privilegeRepository.findByName(privilegeName);

        List<String> privileges = new ArrayList<>();

        if(!(role == null) && !(privilege == null)){
            for (Privilege privilege1:
                    role.getPrivileges()) {
                privileges.add(privilege1.getName());
            }
            if(!(privileges.contains(privilegeName))) {
                roleRepository.addPrivilege(role.getId(), privilege.getId());
                return HttpServletResponse.SC_OK;
            }
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return  HttpServletResponse.SC_BAD_REQUEST;
    }


    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping("/removeprivilegefromrole/{roleName}/{privilegeName}")
    public int removePrivilegeFromRole(@PathVariable("roleName") String roleName, @PathVariable("privilegeName") String privilegeName, HttpServletResponse response){

        roleName = roleName.toUpperCase();
        privilegeName = privilegeName.toUpperCase();
        if(!roleName.matches("^ROLE_")) roleName = "ROLE_" + roleName.toUpperCase();
        if(!privilegeName.matches("_PRIVILEGE$")) privilegeName = privilegeName.toUpperCase() + "_PRIVILEGE";

        Role role = roleRepository.findByName(roleName);
        Privilege privilege = privilegeRepository.findByName(privilegeName);

        List<String> privileges = new ArrayList<>();

        if(!(role == null) && !(privilege == null)){
            for (Privilege privilege1:
                    role.getPrivileges()) {
                privileges.add(privilege1.getName());
            }
            if(privileges.contains(privilegeName)) {
                roleRepository.removePrivilege(role.getId(), privilege.getId());
                return HttpServletResponse.SC_OK;
            }
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return  HttpServletResponse.SC_BAD_REQUEST;
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PutMapping("/addroletouser/{userName}/{roleName}")
    public int addRoleToUser(@PathVariable("userName") String userName, @PathVariable("roleName") String roleName, HttpServletResponse response){

        roleName = roleName.toUpperCase();
        if(!roleName.matches("^ROLE_")) roleName = "ROLE_" + roleName.toUpperCase();

        Role role = roleRepository.findByName(roleName);
        JWTuser jwTuser = jwTuserRepository.findByUsername(userName);

        List<String> roles = new ArrayList<>();

        if(!(jwTuser == null) && !(role == null)){
            for (Role role1:
                    jwTuser.getRoles()) {
                roles.add(role1.getName());
            }
            if(!(roles.contains(roleName))) {
                jwTuserRepository.addRole(jwTuser.getId(), role.getId());
                return HttpServletResponse.SC_OK;
            }
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return  HttpServletResponse.SC_BAD_REQUEST;
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping("/removerolefromuser/{userName}/{roleName}")
    public int removeRoleFromUser(@PathVariable("userName") String userName, @PathVariable("roleName") String roleName, HttpServletResponse response){

        roleName = roleName.toUpperCase();
        if(!roleName.matches("^ROLE_")) roleName = "ROLE_" + roleName.toUpperCase();

        Role role = roleRepository.findByName(roleName);
        JWTuser jwTuser = jwTuserRepository.findByUsername(userName);

        List<String> roles = new ArrayList<>();

        if(!(jwTuser == null) && !(role == null)){
            for (Role role1:
                    jwTuser.getRoles()) {
                roles.add(role1.getName());
            }
            if(roles.contains(roleName)) {
                jwTuserRepository.removeRole(jwTuser.getId(), role.getId());
                return HttpServletResponse.SC_OK;
            }
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return  HttpServletResponse.SC_BAD_REQUEST;
    }

    @PostMapping("/sign-up")
    public int signUp(@RequestBody JWTuser jwTuser, HttpServletResponse response){
        JWTuser jwTuser1 = jwTuserRepository.findByUsername(jwTuser.getUsername());
        if(jwTuser1 == null){
            jwTuser.setPassword(encoder.encode(jwTuser.getPassword()));
            jwTuserRepository.save(jwTuser);
            return HttpServletResponse.SC_OK;
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return  HttpServletResponse.SC_BAD_REQUEST;
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping("/deleteuser/{username}")
    public int removeUser(@PathVariable String username, HttpServletResponse response){

        JWTuser jwTuser = jwTuserRepository.findByUsername(username);

        if(!(jwTuser == null)){
            jwTuserRepository.removeRoleConstraint(jwTuser.getId());
            jwTuserRepository.delete(jwTuser);
            return HttpServletResponse.SC_OK;
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return  HttpServletResponse.SC_BAD_REQUEST;
    }



    @RequestMapping("/getuserbyusername/{username}")
    public JWTuser getByUsername(@PathVariable String username, HttpServletResponse response){
        JWTuser jwTuser = jwTuserRepository.findByUsername(username);
        if(!(jwTuser == null)){
            return jwTuser;
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return  null;
    }

    @RequestMapping("/getallusers")
    public List<JWTuser> getAllUsers(HttpServletResponse response){
        List<JWTuser> jwTusers = jwTuserRepository.findAll();
        if(!(jwTusers.isEmpty())){
            return jwTusers;
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return  null;
    }

    @RequestMapping("/getuserroles/{username}")
    public Collection<Role> getUserRoles(@PathVariable String username, HttpServletResponse response){
        JWTuser jwTuser = jwTuserRepository.findByUsername(username);
        if(!(jwTuser == null)){
            return jwTuser.getRoles();
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return  null;
    }

    @RequestMapping("/getuserprivileges/{username}")
    public Collection<Privilege> getUserPrivileges(@PathVariable String username, HttpServletResponse response){
        JWTuser jwTuser = jwTuserRepository.findByUsername(username);
        if(!(jwTuser == null)){
            Collection<Privilege> privileges = new ArrayList<>();
            for (Role role:
                 jwTuser.getRoles()) {
                privileges.addAll(role.getPrivileges());
            }
            return privileges;
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return  null;
    }

    @RequestMapping(value = "/getroleprivileges/{roleName}")
    public Collection<Privilege> getRolePrivileges(@PathVariable String roleName, HttpServletResponse response){
        roleName = roleName.toUpperCase();
        if(!roleName.matches("^ROLE_")) roleName = "ROLE_" + roleName.toUpperCase();

        Role role = roleRepository.findByName(roleName);
        if(!(role == null)){
            return role.getPrivileges();
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return  null;
    }


}
