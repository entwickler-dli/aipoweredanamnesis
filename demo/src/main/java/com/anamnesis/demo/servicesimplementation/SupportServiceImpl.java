package com.anamnesis.demo.servicesimplementation;

import com.anamnesis.demo.config.EmailSenderConfig;
import  com.anamnesis.demo.serviceinteface.SupportService;
import org.openapitools.model.SupportRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static  com.anamnesis.demo.DemoApplication.log;

@Service
public class SupportServiceImpl implements SupportService {

    @Autowired
    private EmailSenderConfig emailSenderConfig;

    @Override
    public ResponseEntity<String> supportRequest(SupportRequestDTO supportRequestDTO) {
        try {
            emailSenderConfig.sendSupportRequest(supportRequestDTO);
        } catch (Exception e) {
            log.info("Support email could not be sent");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email cannot be sent");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Support email was sent");
    }

}
