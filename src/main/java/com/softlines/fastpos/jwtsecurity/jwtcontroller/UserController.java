package com.softlines.fastpos.jwtsecurity.jwtcontroller;


import com.fasterxml.jackson.core.JsonParseException;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.jwtsecurity.securitydomain.DbInfo;
import com.softlines.fastpos.exceptionmanagement.ExceptionHandling;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

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
    @Autowired

    ExceptionManagement exceptionManagement;

     ExceptionHandling exceptionHandling;

    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }
        return errors;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(JsonParseException.class)
    public String handleJsonParseException(
            JsonParseException ex) {
        return ex.getMessage();
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody UserDTO userDTO){
        try {
            Optional<JWTuser> existingUser = jwTuserRepository.findById(userDTO.getId());
            if(existingUser.isPresent()){
                return ResponseEntity.noContent().build();
            }

            JWTuser jwTuser = userMapper.toJWTuser(userDTO);
            jwTuser.setPassword(encoder.encode(jwTuser.getPassword()));
            JWTuser createdUser = jwTuserRepository.save(jwTuser);
            userDTO.setId(createdUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        }catch (Exception e){
            return exceptionManagement.getResponseEntityAccordingToException(e);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @GetMapping("/getall")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        try {
            List<JWTuser> allUsers = jwTuserRepository.findAllUsers();
            return ResponseEntity.ok().body(userMapper.toUserDTOs(allUsers));
        }catch (Exception e){
            return exceptionHandling.getResponseEntityAccordingToException(e);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @GetMapping("/get/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username){
        try {
            JWTuser jwTuser = jwTuserRepository.findByUsername(username);
            if(jwTuser != null) {
                var userDTO = userMapper.toUserDto(jwTuser);
                return ResponseEntity.ok().body(userDTO);
            }
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return exceptionHandling.getResponseEntityAccordingToException(e);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<UserDTO> deleteUserById(@PathVariable("userId") long userId, @RequestBody UserDTO userDTO){
        try {
            Optional<JWTuser> existingUser = jwTuserRepository.findById(userDTO.getId());
            if(existingUser.isPresent()){
                jwTuserRepository.delete(existingUser.get());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(userDTO);
            }
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return exceptionHandling.getResponseEntityAccordingToException(e);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PutMapping("/put/{userId}")
    public ResponseEntity<UserDTO> editUserById(@PathVariable("userId") long userId, @RequestBody UserDTO userDTO){
        try {
            Optional<JWTuser> existingUser = jwTuserRepository.findById(userDTO.getId());
            if(existingUser.isPresent()){

                String newPassword = userDTO.getPassword();
                String oldPassword = existingUser.get().getPassword();

                if(!newPassword.equals(oldPassword))
                userDTO.setPassword(encoder.encode(newPassword));

                jwTuserRepository.save(userMapper.toJWTuser(userDTO));
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(userDTO);
            }
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return exceptionHandling.getResponseEntityAccordingToException(e);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @GetMapping("/getroles/{userId}")
    public ResponseEntity<List<RoleDTO>> getUserRoles(@PathVariable("userId") long userId){
        try {
            Optional<JWTuser> jwTuser = jwTuserRepository.findById(userId);
            if(jwTuser.isPresent()){
                List<Long> roleIds = userMapper.toUserDto(jwTuser.get()).getRoleIds();
                return  ResponseEntity.status(HttpStatus.OK).body(roleMapper.toRoleDTOs(roleRepository.findAllById(roleIds)));
            }
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return exceptionHandling.getResponseEntityAccordingToException(e);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @GetMapping("/getprivileges/{userId}")
    public ResponseEntity<List<Privilege>> getUserPrivileges(@PathVariable("userId") long userId){
        try {
            Optional<JWTuser> jwTuser = jwTuserRepository.findById(userId);
            if(jwTuser.isPresent()){
                List<Long> roleIds = userMapper.toUserDto(jwTuser.get()).getRoleIds();
                List<RoleDTO> roleDTOS = roleMapper.toRoleDTOs(roleRepository.findAllById(roleIds));
                List<Privilege> privileges = new ArrayList<>();
                for (RoleDTO roleDTO :
                        roleDTOS) {
                    privileges.addAll(privilegeRepository.findAllById(roleDTO.getPrivilegeIds()));
                }
                return  ResponseEntity.status(HttpStatus.ACCEPTED).body(privileges);
            }
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return exceptionHandling.getResponseEntityAccordingToException(e);
        }
    }

}
