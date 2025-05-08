package com.anamnesis.demo.serviceinteface;

import org.openapitools.model.SupportRequestDTO;
import org.springframework.http.ResponseEntity;

public interface SupportService {
    ResponseEntity<String> supportRequest(SupportRequestDTO supportRequestDTO);
}
