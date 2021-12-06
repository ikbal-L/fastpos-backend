package com.softlines.fastpos.security.controllers;


import com.softlines.fastpos.exceptionmanagement.ExceptionHandling;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.security.securitydomain.User;
import com.softlines.fastpos.security.securitydomain.Privilege;
import com.softlines.fastpos.security.securitydomain.securitydto.RoleDTO;
import com.softlines.fastpos.security.securitydomain.securitydto.UserDTO;
import com.softlines.fastpos.security.securitydomain.securitymapper.RoleMapper;
import com.softlines.fastpos.security.securitydomain.securitymapper.UserMapper;
import com.softlines.fastpos.security.securityrepository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/config/user")
public class UserController {

    @Autowired
    UserRepository userRepository;
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

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @PostMapping(value = "/save", headers = "Content-Type=application/json")
    public ResponseEntity<Long> addUser(@Valid @RequestBody UserDTO userDTO){
        try {
            Optional<User> existingUser = userRepository.findById(userDTO.getId());
            if(existingUser.isPresent()){
                return ResponseEntity.noContent().build();
            }

            User user = userMapper.toJWTuser(userDTO);
            user.setPassword(encoder.encode(user.getPassword()));
            User createdUser = userRepository.save(user);
//            userDTO.setId(createdUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser.getId());
        }catch (Exception e){
            return exceptionManagement.getResponseEntityAccordingToException(e);
        }
    }

//    @PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @GetMapping("/getall")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        try {
            List<User> allUsers = userRepository.findAllUsers();
            var userDtos = userMapper.toUserDTOs(allUsers);
            return ResponseEntity.ok().body(userDtos);
        }catch (Exception e){
            return exceptionHandling.getResponseEntityAccordingToException(e);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @GetMapping("/get/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username){
        try {
            User user = userRepository.findByUsername(username);
            if(user != null) {
                var userDTO = userMapper.toUserDto(user);
                return ResponseEntity.ok().body(userDTO);
            }
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }catch (Exception e){
            return exceptionHandling.getResponseEntityAccordingToException(e);
        }
    }

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<UserDTO> deleteUserById(@PathVariable("userId") long userId, @RequestBody UserDTO userDTO){
        try {
            Optional<User> existingUser = userRepository.findById(userDTO.getId());
            if(existingUser.isPresent()){
                userRepository.delete(existingUser.get());
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
            Optional<User> existingUser = userRepository.findById(userDTO.getId());
            if(existingUser.isPresent()){

                String newPassword = userDTO.getPassword();
                String oldPassword = existingUser.get().getPassword();

                if (newPassword!= null){
                    var encodedNewPassword = encoder.encode(newPassword);
                    if( !encodedNewPassword.equals(oldPassword)){
                        userDTO.setPassword(encodedNewPassword);
                    }
                }else {
                    userDTO.setPassword(oldPassword);
                }


                userRepository.save(userMapper.toJWTuser(userDTO));
                return ResponseEntity.ok(userDTO);
                
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
            Optional<User> jwTuser = userRepository.findById(userId);
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
            Optional<User> jwTuser = userRepository.findById(userId);
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

    //@PreAuthorize("@apiAuth.checkRoles(authentication, 'ROLE_ADMIN')")


    @GetMapping("/getannexes/{userId}")
    public ResponseEntity<List<Long>> getUserAnnexes(@PathVariable("userId") long userId){
        try {
            Optional<User> jwTuser = userRepository.findByIdWithAnnexes(userId);
            if(jwTuser.isPresent()){
                var user = jwTuser.get();
                System.out.println(user.getEmail());
                System.out.println(user.getFirstName());
//                List<Long> annexesIds = userMapper.toUserDto(user).getAnnexesIds();
                List<Long> annexesIds = userMapper.toUserDto(jwTuser.get()).getAnnexesIds();
//                return  ResponseEntity.status(HttpStatus.OK).body(annexesIds);
                return  ResponseEntity.status(HttpStatus.OK).body(annexesIds);
            }
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return exceptionHandling.getResponseEntityAccordingToException(e);
        }
    }

}
