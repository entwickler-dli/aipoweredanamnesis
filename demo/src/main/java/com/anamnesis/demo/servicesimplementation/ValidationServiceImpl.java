package com.anamnesis.demo.servicesimplementation;

import com.anamnesis.demo.config.EmailSenderConfig;
import com.anamnesis.demo.dao.User;
import com.anamnesis.demo.helper.EmailCodeHelper;
import com.anamnesis.demo.helper.RegistrationHelper;
import com.anamnesis.demo.repository.UserRepository;
import com.anamnesis.demo.serviceinteface.ValidationService;
import org.openapitools.model.ForgetPasswordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.anamnesis.demo.DemoApplication.log;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailCodeHelper emailCodeHelper;

    @Autowired
    private RegistrationHelper registrationHelper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailSenderConfig emailSenderConfig;

    @Override
    public ResponseEntity<String> forgetPassword(ForgetPasswordDTO forgetPasswordDTO) {
        if (forgetPasswordDTO.getPassword() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New passwords cannot be empty");
        }

        if (!registrationHelper.isValidFormatPassword(forgetPasswordDTO.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password has a wrong format");
        }

        if (!forgetPasswordDTO.getPassword().equals(forgetPasswordDTO.getPasswordConfirmation())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New passwords do not match");
        }

        String emailDecoded = emailCodeHelper.decodeEmail(forgetPasswordDTO.getEmail());
        if(userRepository.findByEmail(emailDecoded).isPresent()){
            User user = userRepository.findByEmail(emailDecoded).get();

            if (passwordEncoder.matches(forgetPasswordDTO.getPassword(), user.getPassword())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password cannot be the same that you used earlier!");
            }

            user.setPassword(passwordEncoder.encode(forgetPasswordDTO.getPassword()));
            userRepository.save(user);

            log.info("Password updated:" + user.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @Override
    public ResponseEntity<String> validateEmail(String email, Integer code) {
        String emailDecoded = emailCodeHelper.decodeEmail(email);
        if(userRepository.findByEmail(emailDecoded).isPresent()){
            User user = userRepository.findByEmail(emailDecoded).get();
            if(user.getVerificationCode() != null) {
                if (user.getVerificationCode().equals(code)) {
                    user.setVerificationCode(null);
                    userRepository.save(user);
                    log.info("User email validated:" + user.getUserId());
                    return ResponseEntity.status(HttpStatus.OK).body("User was successfully verified");
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The user's verification codes were not matching");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The user is already verified");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User cannot be verified");
    }

    @Override
    public ResponseEntity<String> forgetPasswordRequest(String email) {
        try {
            emailSenderConfig.sendForgetPasswordEmail(email);
        } catch (Exception e) {
            System.out.println(e); //TODO delete this line
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email cannot be sent");
        }
        log.info("User forgot password:");
        return ResponseEntity.status(HttpStatus.OK).body("Email was sent");
    }
}
