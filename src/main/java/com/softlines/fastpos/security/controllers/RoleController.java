package com.softlines.fastpos.security.controllers;

import com.softlines.fastpos.exceptionmanagement.ExceptionHandling;
import com.softlines.fastpos.security.securitydomain.Role;
import com.softlines.fastpos.security.securitydomain.securitydto.RoleDTO;
import com.softlines.fastpos.security.securitydomain.securitymapper.RoleMapper;
import com.softlines.fastpos.security.securityrepository.PrivilegeRepository;
import com.softlines.fastpos.security.securityrepository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/config/role")
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
    public ResponseEntity<Long> saveRole(@RequestBody RoleDTO roleDTO) {
        try {
            String roleName = roleDTO.getName().toUpperCase();
            if (!roleName.matches("^ROLE_")) roleName = "ROLE_" + roleName;
            roleDTO.setName(roleName);
            Optional<Role> existingRole = roleRepository.findRoleById(roleDTO.getId());
            if (existingRole.isPresent()) {
                return ResponseEntity.noContent().build();
            }
            Role createdRole = roleRepository.save(roleMapper.toRole(roleDTO));

            return ResponseEntity.status(HttpStatus.CREATED).body(createdRole.getId());
        } catch (Exception exception) {
            return exceptionHandling.getResponseEntityAccordingToException(exception);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<RoleDTO> deleteRole(@Valid @PathVariable long id) {
        try {
            Optional<Role> roleToDelete = roleRepository.findRoleById(id);
            if (roleToDelete.isPresent()) {
                roleRepository.deleteById(id);
                var roleDTO = roleMapper.toRoleDto(roleToDelete.get());
                return ResponseEntity.status(HttpStatus.OK).body(roleDTO);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception exception) {
            return exceptionHandling.getResponseEntityAccordingToException(exception);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PutMapping("/put/{id}")
    public ResponseEntity<RoleDTO> editRole(@Valid @PathVariable long id, @RequestBody RoleDTO roleDTO) {
        try {
            Optional<Role> existingRole = roleRepository.findRoleById(roleDTO.getId());
            if (existingRole.isPresent()) {
                Role role = roleMapper.toRole(roleDTO);
                return ResponseEntity.ok().body(roleMapper.toRoleDto(roleRepository.save(role)));
            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionHandling.getResponseEntityAccordingToException(exception);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @GetMapping("/getall")
    public ResponseEntity getRoles() {
        try {
            List<Role> roles = roleRepository.findAllRolesWithPrivileges();

            if (roles == null || roles.isEmpty())
                return ResponseEntity.noContent().build();
            var roleDtos = roleMapper.toRoleDTOs(roles);
            return ResponseEntity.ok().body(roleDtos);
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
            return exceptionHandling.getResponseEntityAccordingToException(exception);
        }
    }
}
