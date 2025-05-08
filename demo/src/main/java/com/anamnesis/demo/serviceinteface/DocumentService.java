package com.anamnesis.demo.serviceinteface;

import org.openapitools.model.DocumentDTO;
import org.springframework.http.ResponseEntity;

public interface DocumentService {
    ResponseEntity<DocumentDTO> getDocument(String type);
}
