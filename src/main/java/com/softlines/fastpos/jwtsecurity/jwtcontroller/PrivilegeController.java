package com.softlines.fastpos.jwtsecurity.jwtcontroller;

import com.softlines.fastpos.exceptionmanagement.ExceptionHandling;
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
import java.util.Optional;

@RestController
@RequestMapping("/privilege")
public class PrivilegeController {

    @Autowired
    PrivilegeRepository privilegeRepository;
    @Autowired
    ExceptionHandling exceptionHandling;

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<Privilege> addPrivilege(@RequestBody Privilege privilege){
        try {
            if(privilege.getName().equalsIgnoreCase("")) privilege.setName(null);
            String privilegeName = privilege.getName().toUpperCase();
            if(!privilegeName.matches("_PRIVILEGE$")) privilegeName = privilegeName + "_PRIVILEGE";
            privilege.setName(privilegeName);

            Privilege existingPrivilege = privilegeRepository.findByName(privilegeName);
            if (existingPrivilege != null) {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }
            Privilege createdPrivilege = privilegeRepository.save(privilege);
            if (createdPrivilege == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdPrivilege);
            }
        } catch (Exception exception) {
            return exceptionHandling.getResponseEntityAccordingToException(exception);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping("/delete/{privilegeId}")
    public ResponseEntity<Privilege> deletePrivilege(@RequestBody Privilege privilege, @PathVariable("privilegeId") long privilegeId){
        try {
            Optional<Privilege> privilegeToDelete = privilegeRepository.findById(privilege.getId());
            if (privilegeToDelete.isPresent()) {
                Privilege privilege1 = privilegeToDelete.get();
                privilegeRepository.delete(privilege1);
                privilege1.setRoles(null);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(privilege1);
            }else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception exception) {
            return exceptionHandling.getResponseEntityAccordingToException(exception);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PutMapping("/put/{privilegeId}")
    public ResponseEntity<Privilege> edit(@PathVariable("privilegeId") long privilegeId, @RequestBody Privilege privilege) {
        try {
            String privilegeName = privilege.getName().toUpperCase();
            if(!privilegeName.matches("_PRIVILEGE$")) privilegeName = privilegeName + "_PRIVILEGE";
            privilege.setName(privilegeName);
            Optional<Privilege> existingPrivilege = privilegeRepository.findById(privilegeId);
            if(existingPrivilege.isPresent()){
                return ResponseEntity.ok().body(privilegeRepository.save(privilege));
            }else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionHandling.getResponseEntityAccordingToException(exception);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @GetMapping("/getall")
    public ResponseEntity<List<Privilege>> getPrivileges() {
        try {
            return ResponseEntity.ok().body(privilegeRepository.findAll());
        } catch (Exception exception) {
            return exceptionHandling.getResponseEntityAccordingToException(exception);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @GetMapping("/getbyid/{id}")
    public ResponseEntity<Privilege> getPrivilegeById(@PathVariable long id) {
        try {
            Optional<Privilege> privilege = privilegeRepository.findById(id);
            if(privilege.isPresent()){
                return ResponseEntity.ok().body(privilege.get());
            }else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception exception) {
            return exceptionHandling.getResponseEntityAccordingToException(exception);
        }
    }

}
