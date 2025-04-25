package com.anamnesis.demo.servicesimplementation;

import com.anamnesis.demo.config.EmailSenderConfig;
import com.anamnesis.demo.dao.User;
import com.anamnesis.demo.helper.EmailCodeHelper;
import com.anamnesis.demo.helper.Mapper;
import com.anamnesis.demo.helper.RegistrationHelper;
import com.anamnesis.demo.repository.UserRepository;
import com.anamnesis.demo.serviceinteface.UserService;
import com.anamnesis.demo.util.AuthService;
import com.anamnesis.demo.util.JwtService;
import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.anamnesis.demo.DemoApplication.log;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @Autowired
    private Mapper mapper;

    @Autowired
    private EmailCodeHelper emailCodeHelper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailSenderConfig emailSenderConfig;

    @Autowired
    private RegistrationHelper registrationHelper;


    @Override
    public ResponseEntity<String> deleteUser(Integer id) {

        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            userRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(Integer id) {

        return userRepository.findById(id)
                .map(userEntity -> {
                    UserDTO user = mapper.toUserDTO(userEntity);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public ResponseEntity<UserDTO> getUserMe(TokenDTO tokenDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if(userRepository.findByEmail(userDetails.getUsername()).isPresent()) {
                User user = userRepository.findByEmail(userDetails.getUsername()).get();
                user.setPassword("");
                return ResponseEntity.status(HttpStatus.OK).body(mapper.toUserDTO(user));

            }

        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<UserListDTO> getUserList() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOList = users.stream()
                .map(userEntity -> mapper.toUserDTO(userEntity))
                .collect(Collectors.toList());

        UserListDTO userListDTO = new UserListDTO();
        userListDTO.data(userDTOList);
        return ResponseEntity.ok(userListDTO);
    }

    @Override
    public ResponseEntity<String> registerUser(UserDTO userDTO) {

        if (registrationHelper.isValidEmail(userDTO.getEmail()) && registrationHelper.isValidFormatPassword(userDTO.getPassword())) {

            if (userRepository.existsByEmail(userDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }
            if (userRepository.existsByPhone(userDTO.getPhone())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Phone number already exists");
            }

            Integer verificationCode = emailCodeHelper.generateEmailCode();

            User user = mapper.toUser(userDTO);
            user.setUserId(null);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setVerificationCode(verificationCode);

            LocalDateTime currentDateTime = LocalDateTime.now();
            Date sqlDate = Date.valueOf(currentDateTime.toLocalDate());
            user.setCreatedAt(sqlDate);
            user.setUpdatedAt(sqlDate);

            try {
                emailSenderConfig.sendVerificationEmail(userDTO.getEmail(), verificationCode);
            } catch (Exception e) {
                log.info("Registration email could not be sent to user");
                return ResponseEntity.internalServerError().build();
            }
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Wrong email or password format");

    }

    @Override
    public ResponseEntity<String> updateUser(Integer id, UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User updatedUser = existingUser.get();
        if (updatedUser.getPassword().equals(userDTO.getPassword())) {
            updatedUser = mapper.toUser(userDTO);

            LocalDateTime currentDateTime = LocalDateTime.now();
            Date sqlDate = Date.valueOf(currentDateTime.toLocalDate());
            updatedUser.setUpdatedAt(sqlDate);

            userRepository.save(updatedUser);
            return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
        }

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("User password does not match");
    }

    @Override
    public ResponseEntity<TokenDTO> userLogin(SignInDTO signInDTO) {
        User authUser = authService.authenticate(signInDTO);

        String jwtToken = jwtService.generateToken(authUser);

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(jwtToken);
        tokenDTO.setExpiry(BigDecimal.valueOf(jwtService.getExpirationTime()));

        return ResponseEntity.status(HttpStatus.OK).body(tokenDTO);

    }

    @Override
    public ResponseEntity<String> userUpdatePassword(Integer id, ChangePasswordDTO changePasswordDTO) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = existingUser.get();

        if (changePasswordDTO.getNewPassword() == null || changePasswordDTO.getNewPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password cannot be empty");
        }

        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Old password is incorrect");
        }

        if (!registrationHelper.isValidFormatPassword(changePasswordDTO.getNewPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password has a wrong format");
        }

        if (changePasswordDTO.getOldPassword().equals(changePasswordDTO.getNewPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The new password cannot be the same that you are using now!");
        }

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getNewPasswordAgain())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<String> updateUserPhone(Integer id, String phone){
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User updatedUser = existingUser.get();

        if (phone != null && !phone.isEmpty()){
            updatedUser.setPhone(phone);
            userRepository.save(updatedUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
