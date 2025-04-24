package com.anamnesis.demo.controller;

import com.anamnesis.demo.serviceinteface.SupportService;
import org.openapitools.api.SupportApi;
import org.openapitools.model.SupportRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SupportController implements SupportApi {

    @Autowired
    private SupportService supportService;

    @Override
    public ResponseEntity<String> supportRequest(SupportRequestDTO supportRequestDTO) {
        return supportService.supportRequest(supportRequestDTO);
    }
}
