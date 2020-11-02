package com.softlines.fastpos.jwtsecurity.jwtcontroller;

import com.softlines.fastpos.exceptionmanagement.ExceptionHandling;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.RoleDTO;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitymapper.RoleMapper;
import com.softlines.fastpos.jwtsecurity.securityrepository.PrivilegeRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ExceptionHandling exceptionHandling;


    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PostMapping(value = "/save", consumes = "application/json")
    public ResponseEntity<RoleDTO> saveRole(@RequestBody RoleDTO roleDTO) {
        try {
            String roleName = roleDTO.getName().toUpperCase();
            if (!roleName.matches("^ROLE_")) roleName = "ROLE_" + roleName;
            roleDTO.setName(roleName);
            Role existingRole = roleRepository.findByName(roleName);
            if (existingRole != null) {
                return ResponseEntity.noContent().build();
            }
            Role createdRole = roleRepository.save(roleMapper.toRole(roleDTO));
            return ResponseEntity.status(HttpStatus.CREATED).body(roleMapper.toRoleDto(createdRole));
        } catch (Exception exception) {
            return exceptionHandling.getResponseEntityAccordingToException(exception);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping(value = "/delete", consumes = "application/json")
    public ResponseEntity<RoleDTO> deleteRole(@RequestBody RoleDTO roleDTO) {
        try {
            Optional<Role> roleToDelete = roleRepository.findById(roleDTO.getId());
            if (roleToDelete.isPresent()) {
                Role role = roleToDelete.get();
                roleRepository.delete(role);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(roleDTO);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PutMapping("/put")
    public ResponseEntity<RoleDTO> editRole(@RequestBody RoleDTO roleDTO) {
        try {
            Optional<Role> existingRole = roleRepository.findById(roleDTO.getId());
            if (existingRole.isPresent()) {
                Role role = roleMapper.toRole(roleDTO);
                return ResponseEntity.ok().body(roleMapper.toRoleDto(roleRepository.save(role)));
            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @GetMapping("/getall")
    public ResponseEntity getRoles() {
        try {
            List<Role> roles = roleRepository.findAllRolesWithPrivileges();

            if (roles == null || roles.isEmpty())
                return ResponseEntity.noContent().build();
            return ResponseEntity.ok().body(roleMapper.toRoleDTOs(roles));
        } catch (Exception exception) {
            return exceptionHandling.getResponseEntityAccordingToException(exception);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @GetMapping("/getbyname/{roleName}")
    public ResponseEntity<RoleDTO> getRoleByName(@PathVariable String roleName) {
        //if(!roleName.toUpperCase().matches("^ROLE_")) roleName = "ROLE_" + roleName.toUpperCase();
        try {
            Role role = roleRepository.findByName(roleName);
            if (role != null)
                return ResponseEntity.ok().body(roleMapper.toRoleDto(role));
            return ResponseEntity.noContent().build();
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }
}
