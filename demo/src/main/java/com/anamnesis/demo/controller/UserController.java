package com.anamnesis.demo.controller;

import com.anamnesis.demo.serviceinteface.UserService;
import com.anamnesis.demo.util.AuthService;
import com.anamnesis.demo.util.JwtService;
import org.openapitools.api.UserApi;
import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {

    @Autowired
    private UserService userService;
    
    private final JwtService jwtService;

    private final AuthService authService;

    public UserController(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @Override
    public ResponseEntity<String> deleteUser(Integer id) {
        return userService.deleteUser(id);
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(Integer id) {
        return userService.getUserById(id);
    }

    @Override
    public ResponseEntity<UserDTO> getUserMe(TokenDTO tokenDTO) {
        return userService.getUserMe(tokenDTO);
    }

    @Override
    public ResponseEntity<UserListDTO> getUserList() {
        return userService.getUserList();
    }

    @Override
    public ResponseEntity<String> registerUser(UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    @Override
    public ResponseEntity<String> updateUser(Integer id, UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    @Override
    public ResponseEntity<TokenDTO> userLogin(SignInDTO signInDTO) {
        return userService.userLogin(signInDTO);
    }

    @Override
    public ResponseEntity<String> userUpdatePassword(Integer id, ChangePasswordDTO changePasswordDTO) {
        return userService.userUpdatePassword(id, changePasswordDTO);
    }

    @Override
    public ResponseEntity<String> updateUserPhone(Integer id, String phone){
        return userService.updateUserPhone(id, phone);
    }
}
