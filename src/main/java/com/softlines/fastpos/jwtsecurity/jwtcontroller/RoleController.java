package com.softlines.fastpos.jwtsecurity.jwtcontroller;

import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.RoleDTO;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitymapper.RoleMapper;
import com.softlines.fastpos.jwtsecurity.securityrepository.PrivilegeRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PrivilegeRepository privilegeRepository;

    @Autowired
    ExceptionManagement exceptionManagement;


    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PostMapping(value = "/save", consumes = "application/json")
    public ResponseEntity<RoleDTO> addRole(@RequestBody RoleDTO roleDTO){

        String roleName = roleDTO.getName().toUpperCase();
        if(!roleName.matches("^ROLE_")) roleName = "ROLE_" + roleName;
        roleDTO.setName(roleName);
        try {
            Optional<Role> existingRole = roleRepository.findById(roleDTO.getId());
            if (existingRole.isPresent()) {
                return ResponseEntity.noContent().build();
            }
            Role createdRole = roleRepository.save(roleMapper.toRole(roleDTO));
            return ResponseEntity.status(HttpStatus.CREATED).body(roleMapper.toRoleDto(createdRole));
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping(value = "/delete", consumes = "application/json")
    public ResponseEntity<RoleDTO> deleteRoleById(@RequestBody RoleDTO roleDTO){
        try {
            Optional<Role> roleToDelete = roleRepository.findById(roleDTO.getId());
            if (roleToDelete.isPresent()) {
                Role role= roleToDelete.get();
                roleRepository.delete(role);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(roleMapper.toRoleDto(role));
            }else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }
//
//    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
//    @DeleteMapping("/deletebyname/{roleName}")
//    public ResponseEntity<RoleDTO> deleteRoleByName(@PathVariable String roleName){
//        roleName = roleName.toUpperCase();
//        if(!roleName.matches("^ROLE_")) roleName = "ROLE_" + roleName;
//        try {
//            Role roleToDelete = roleRepository.findByName(roleName);
//            if (roleToDelete != null) {
//                roleRepository.removeUserConstraint(roleToDelete.getId());
//                roleRepository.removePrivilegeConstraint(roleToDelete.getId());
//                roleRepository.delete(roleToDelete);
//                return ResponseEntity.status(HttpStatus.ACCEPTED).body(roleMapper.toRoleDto(roleToDelete));
//            }else {
//                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(roleMapper.toRoleDto(roleToDelete));
//            }
//        } catch (Exception exception) {
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND, " Not Found", exception);
//        }
//    }

//    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
//    @PutMapping("/addprivilege/{roleId}")
//    public ResponseEntity<RoleDTO> addPrivilegeToRole(@PathVariable("roleId") long roleId, @RequestBody Privilege privilege){
//        String privilegeName = privilege.getName().toUpperCase();
//        if(!privilegeName.matches("_PRIVILEGE$")) privilegeName = privilegeName + "_PRIVILEGE";
//        privilege.setName(privilegeName);
//        try {
//            Role role = roleRepository.findById(roleId).get();
//            Privilege privilegeToAdd = privilegeRepository.findByName(privilegeName);
//
//            if (role != null && privilegeToAdd != null && !(roleMapper.toRoleDto(role).getPrivilegeIds().contains(privilegeToAdd.getId()))) {
//                roleRepository.addPrivilege(roleId, privilegeToAdd.getId());
//                return ResponseEntity.status(HttpStatus.ACCEPTED).body(roleMapper.toRoleDto(roleRepository.findById(roleId).get()));
//            }else {
//                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(roleMapper.toRoleDto(role));
//            }
//        } catch (Exception exception) {
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND, " Not Found", exception);
//        }
//    }
//
//    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
//    @DeleteMapping("/removeprivilege/{roleId}")
//    public ResponseEntity<RoleDTO> removePrivilegeFromRole(@PathVariable("roleId") long roleId, @RequestBody Privilege privilege){
//        String privilegeName = privilege.getName().toUpperCase();
//        if(!privilegeName.matches("_PRIVILEGE$")) privilegeName = privilegeName + "_PRIVILEGE";
//        privilege.setName(privilegeName);
//        try {
//            Role role = roleRepository.findById(roleId).get();
//            Privilege privilegeToRemove = privilegeRepository.findByName(privilegeName);
//
//            if (role != null && privilegeToRemove != null && (roleMapper.toRoleDto(role).getPrivilegeIds().contains(privilegeToRemove.getId()))) {
//                roleRepository.removePrivilege(roleId, privilegeToRemove.getId());
//                return ResponseEntity.status(HttpStatus.ACCEPTED).body(roleMapper.toRoleDto(roleRepository.findById(roleId).get()));
//            }else {
//                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(roleMapper.toRoleDto(role));
//            }
//        } catch (Exception exception) {
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND, " Not Found", exception);
//        }
//    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PutMapping("/put")
    public ResponseEntity<RoleDTO> editRoleById(@RequestBody RoleDTO roleDTO) {
        String roleName = roleDTO.getName().toUpperCase();
        if(!roleName.matches("^ROLE_")) roleName = "ROLE_" + roleName;
        roleDTO.setName(roleName);
        try {
            Optional<Role> existingRole = roleRepository.findById(roleDTO.getId());
            if(existingRole.isPresent()){
                Role role=roleMapper.toRole(roleDTO);
                return ResponseEntity.ok().body(roleMapper.toRoleDto(roleRepository.save(role)));
            }else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @GetMapping("/getall")
    public ResponseEntity<List<RoleDTO>> getRoles() {
        try {
            List<Role> roles = roleRepository.findAllPrivileges();
            return ResponseEntity.ok().body(roleMapper.toRoleDTOs(roles));
        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @GetMapping("/getbyname/{roleName}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable String roleName) {
        if(!roleName.matches("^ROLE_")) roleName = "ROLE_" + roleName;
        try {
            Role role = roleRepository.findByName(roleName);
            if(role != null){
                return ResponseEntity.ok().body(roleMapper.toRoleDto(role));
            }else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }
}
