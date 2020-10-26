package com.softlines.fastpos.jwtsecurity.jwtcontroller;

import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.RoleDTO;
import com.softlines.fastpos.jwtsecurity.securityrepository.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/privilege")
public class PrivilegeController {

    @Autowired
    PrivilegeRepository privilegeRepository;

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<Privilege> addPrivilege(@RequestBody Privilege privilege){

        String privilegeName = privilege.getName().toUpperCase();
        if(!privilegeName.matches("_PRIVILEGE$")) privilegeName = privilegeName + "_PRIVILEGE";
        privilege.setName(privilegeName);

        try {
            Privilege existingPrivilege = privilegeRepository.findByName(privilegeName);
            if (existingPrivilege != null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(existingPrivilege);
            }
            Privilege createdPrivilege = privilegeRepository.save(privilege);
            if (createdPrivilege == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdPrivilege);
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping("/delete/{privilegeId}")
    public ResponseEntity<Privilege> deletePrivilegeById(@PathVariable long privilegeId){
        try {
            Privilege privilegeToDelete = privilegeRepository.findById(privilegeId).get();
            if (privilegeToDelete != null) {
                privilegeRepository.removeConstraint(privilegeId);
                privilegeRepository.delete(privilegeToDelete);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(privilegeToDelete);
            }else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(privilegeToDelete);
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping("/delete/{privilegeName}")
    public ResponseEntity<Privilege> deletePrivilegeByName(@PathVariable String privilegeName){
        privilegeName = privilegeName.toUpperCase();
        if(!privilegeName.matches("_PRIVILEGE$")) privilegeName = privilegeName + "_PRIVILEGE";
        try {
            Privilege privilegeToDelete = privilegeRepository.findByName(privilegeName);
            if (privilegeToDelete != null) {
                privilegeRepository.removeConstraint(privilegeToDelete.getId());
                privilegeRepository.delete(privilegeToDelete);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(privilegeToDelete);
            }else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(privilegeToDelete);
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PutMapping("/edit/{privilegeId}")
    public ResponseEntity<Privilege> getRoles(@PathVariable("privilegeId") long privilegeId, @RequestBody Privilege privilege) {
        String privilegeName = privilege.getName().toUpperCase();
        if(!privilegeName.matches("_PRIVILEGE$")) privilegeName = privilegeName + "_PRIVILEGE";
        privilege.setName(privilegeName);
        try {
            Privilege existingPrivilege = privilegeRepository.findById(privilegeId).get();
            if(existingPrivilege != null){
                privilegeRepository.updatePrivilegeName(privilege.getName(), privilegeId);
                return ResponseEntity.ok().body(privilegeRepository.findById(privilegeId).get());
            }else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(privilege);
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @GetMapping("/getall")
    public ResponseEntity<List<Privilege>> getPrivileges() {
        try {
            return ResponseEntity.ok().body(privilegeRepository.findAll());
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @GetMapping("/getbyid/{id}")
    public ResponseEntity<Privilege> getPrivilegeById(@PathVariable long id) {
        try {
            Privilege privilege = privilegeRepository.findById(id).get();
            if(privilege != null){
                return ResponseEntity.ok().body(privilege);
            }else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(privilege);
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

}
