package com.anamnesis.demo.serviceinteface;

import org.openapitools.model.*;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<String> deleteUser(Integer id);

    ResponseEntity<UserDTO> getUserById(Integer id);

    ResponseEntity<UserListDTO> getUserList();

    ResponseEntity<SearchDTO> getUserLastSearch(Integer id);

    ResponseEntity<UserDTO> getUserMe(TokenDTO tokenDTO);

    ResponseEntity<String> addFavoriteToUser(Integer userId, Integer flatId);

    ResponseEntity<String> removeFavoriteFromUser(Integer userId, Integer flatId);

    ResponseEntity<String> registerUser(UserDTO userDTO);

    ResponseEntity<String> updateUser(Integer id, UserDTO userDTO);

    ResponseEntity<TokenDTO> userLogin(SignInDTO signInDTO);

    ResponseEntity<String> userUpdatePassword(Integer id, ChangePasswordDTO changePasswordDTO);

    ResponseEntity<String> changeUserSubscriptionType(Integer id, String body);

    ResponseEntity<String> updateUserPhone(Integer id, String phone);
}
