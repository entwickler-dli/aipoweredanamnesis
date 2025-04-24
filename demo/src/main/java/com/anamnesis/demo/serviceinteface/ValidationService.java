package com.anamnesis.demo.serviceinteface;

import org.openapitools.model.ForgetPasswordDTO;
import org.springframework.http.ResponseEntity;

public interface ValidationService {
    ResponseEntity<String> forgetPassword(ForgetPasswordDTO forgetPasswordDTO);

    ResponseEntity<String> validateEmail(String email, Integer code);

    ResponseEntity<String> forgetPasswordRequest(String email);
}
