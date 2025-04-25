package com.anamnesis.demo.controller;

import com.anamnesis.demo.serviceinteface.ValidationService;
import org.openapitools.api.ValidationApi;
import org.openapitools.model.ForgetPasswordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValidationController implements ValidationApi {

    @Autowired
    private ValidationService validationService;

    @Override
    public ResponseEntity<String> forgetPassword(ForgetPasswordDTO forgetPasswordDTO) {
        return validationService.forgetPassword(forgetPasswordDTO);
    }

    @Override
    public ResponseEntity<String> forgetPasswordRequest(String email) {
        return validationService.forgetPasswordRequest(email);
    }

    @Override
    public ResponseEntity<String> validateEmail(String email, Integer code) {
        return validationService.validateEmail(email, code);
    }

}
