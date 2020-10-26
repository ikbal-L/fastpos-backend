package com.softlines.fastpos.jwtsecurity.jwtcontroller;


import com.softlines.fastpos.jwtsecurity.securitydomain.DbInfo;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.RoleDTO;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitydto.UserDTO;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitymapper.RoleMapper;
import com.softlines.fastpos.jwtsecurity.securitydomain.securitymapper.UserMapper;
import com.softlines.fastpos.jwtsecurity.securityrepository.DbInfoRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.PrivilegeRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    JWTuserRepository jwTuserRepository;
    @Autowired
    PrivilegeRepository privilegeRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    DbInfoRepository dbInfoRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleMapper roleMapper;

    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<UserDTO> addUser(@RequestBody JWTuser jwTuser){
        try {
            JWTuser existingUser = jwTuserRepository.findByUsername(jwTuser.getUsername());
            if(existingUser != null){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userMapper.toUserDto(existingUser));
            }
            jwTuser.setPassword(encoder.encode(jwTuser.getPassword()));
            JWTuser createdUser = jwTuserRepository.save(jwTuser);
            if (createdUser == null) {
                return ResponseEntity.notFound().build();
            } else {
                UserDTO userDTO = userMapper.toUserDto(createdUser);
                return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
            }
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", e);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @GetMapping("/getall")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        try {
            return ResponseEntity.ok().body(userMapper.toUserDTOs(jwTuserRepository.findAll()));
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", e);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<UserDTO> deleteUserById(@PathVariable("userId") long userId){
        try {
            JWTuser existingUser = jwTuserRepository.findById(userId).get();
            if(existingUser != null){
                jwTuserRepository.removeRoleConstraint(userId);
                jwTuserRepository.delete(existingUser);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(userMapper.toUserDto(existingUser));
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userMapper.toUserDto(existingUser));
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", e);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PutMapping("/edit/{userId}")
    public ResponseEntity<UserDTO> editUserById(@PathVariable("userId") long userId, @RequestBody JWTuser jwTuser){
        try {
            JWTuser existingUser = jwTuserRepository.findById(userId).get();
            if(existingUser != null){
                if(jwTuser.getUsername() != null){
                    String username = jwTuser.getUsername();
                    jwTuserRepository.updateUserName(username, userId);
                }
                if(jwTuser.getPassword() != null){
                    String password = encoder.encode(jwTuser.getPassword());
                    jwTuserRepository.updatePassword(password, userId);
                }
                if(jwTuser.getPinCode() != null){
                    String pinCode = jwTuser.getPinCode();
                    jwTuserRepository.updatePinCode(pinCode, userId);
                }
                if(jwTuser.getFirstName() != null){
                    String firstname = jwTuser.getFirstName();
                    jwTuserRepository.updateFirstname(firstname, userId);
                }
                if(jwTuser.getLastName() != null){
                    String lastName = jwTuser.getLastName();
                    jwTuserRepository.updateLastname(lastName, userId);
                }
                if(jwTuser.getEmail() != null){
                    String email = jwTuser.getEmail();
                    jwTuserRepository.updateEmail(email, userId);
                }
                if(jwTuser.isEnabled() != existingUser.isEnabled()){
                    Boolean enabled = jwTuser.isEnabled();
                    jwTuserRepository.updateIsEnabled(enabled, userId);
                }
                if(jwTuser.isTokenExpired() != existingUser.isTokenExpired()){
                    Boolean tokenExpired = jwTuser.isTokenExpired();
                    jwTuserRepository.updateTokenExpired(tokenExpired, userId);
                }

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(userMapper.toUserDto(jwTuserRepository.findById(userId).get()));
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userMapper.toUserDto(existingUser));
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", e);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PutMapping("/addrole/{userId}/{roleId}")
    public ResponseEntity<UserDTO> addRoleToUserById(@PathVariable("userId") long userId, @PathVariable("roleId") long roleId){
        try {
            JWTuser jwTuser = jwTuserRepository.findById(userId).get();
            Role role = roleRepository.findById(roleId).get();
            if(jwTuser != null && role != null && !(userMapper.toUserDto(jwTuser).getRoleIds().contains(roleId))){
                jwTuserRepository.addRole(userId, roleId);
                return  ResponseEntity.status(HttpStatus.ACCEPTED).body(userMapper.toUserDto(jwTuserRepository.findById(userId).get()));
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userMapper.toUserDto(jwTuserRepository.findById(userId).get()));
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", e);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping("/removerole/{userId}/{roleId}")
    public ResponseEntity<UserDTO> removeRoleFromUserById(@PathVariable("userId") long userId, @PathVariable("roleId") long roleId){
        try {
            JWTuser jwTuser = jwTuserRepository.findById(userId).get();
            Role role = roleRepository.findById(roleId).get();
            if(jwTuser != null && role != null && (userMapper.toUserDto(jwTuser).getRoleIds().contains(roleId))){
                jwTuserRepository.removeRole(userId, roleId);
                return  ResponseEntity.status(HttpStatus.ACCEPTED).body(userMapper.toUserDto(jwTuserRepository.findById(userId).get()));
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userMapper.toUserDto(jwTuserRepository.findById(userId).get()));
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", e);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @GetMapping("/getroles/{userId}")
    public ResponseEntity<List<RoleDTO>> getUserRoles(@PathVariable("userId") long userId){
        try {
            JWTuser jwTuser = jwTuserRepository.findById(userId).get();
            if(jwTuser != null){
                List<Long> roleIds = userMapper.toUserDto(jwTuser).getRoleIds();
                return  ResponseEntity.status(HttpStatus.ACCEPTED).body(roleMapper.toRoleDTOs(roleRepository.findAllById(roleIds)));
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", e);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @GetMapping("/getprivileges/{userId}")
    public ResponseEntity<List<Privilege>> getUserPrivileges(@PathVariable("userId") long userId){
        try {
            JWTuser jwTuser = jwTuserRepository.findById(userId).get();
            if(jwTuser != null){
                List<Long> roleIds = userMapper.toUserDto(jwTuser).getRoleIds();
                List<RoleDTO> roleDTOS = roleMapper.toRoleDTOs(roleRepository.findAllById(roleIds));
                List<Privilege> privileges = new ArrayList<>();
                for (RoleDTO roleDTO :
                        roleDTOS) {
                    privileges.addAll(privilegeRepository.findAllById(roleDTO.getPrivilegeIds()));
                }
                return  ResponseEntity.status(HttpStatus.ACCEPTED).body(privileges);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", e);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PutMapping("/adddbinfo/{userId}/{dbInfoId}")
    public ResponseEntity<UserDTO> addDbInfoToUserById(@PathVariable("userId") long userId, @PathVariable("dbInfoId") long dbInfoId){
        try {
            JWTuser jwTuser = jwTuserRepository.findById(userId).get();
            DbInfo dbInfo = dbInfoRepository.findById(dbInfoId).get();
            if(jwTuser != null && dbInfo != null){
                if(jwTuser.getDbInfo() == null){
                    jwTuserRepository.addDbInfo(userId, dbInfoId);
                }else{
                    if(jwTuser.getDbInfo().getId() != dbInfoId)
                        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userMapper.toUserDto(jwTuserRepository.findById(userId).get()));
                }
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(userMapper.toUserDto(jwTuserRepository.findById(userId).get()));
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userMapper.toUserDto(jwTuserRepository.findById(userId).get()));
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", e);
        }
    }

    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping("/removedbinfo/{userId}/{dbInfoId}")
    public ResponseEntity<UserDTO> removeDbInfoFromUserById(@PathVariable("userId") long userId, @PathVariable("dbInfoId") long dbInfoId){
        try {
            JWTuser jwTuser = jwTuserRepository.findById(userId).get();
            DbInfo dbInfo = dbInfoRepository.findById(dbInfoId).get();
            if(jwTuser != null && dbInfo != null){
                jwTuserRepository.removeDbInfo(userId, dbInfoId);
                return  ResponseEntity.status(HttpStatus.ACCEPTED).body(userMapper.toUserDto(jwTuserRepository.findById(userId).get()));
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userMapper.toUserDto(jwTuserRepository.findById(userId).get()));
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", e);
        }
    }

}
