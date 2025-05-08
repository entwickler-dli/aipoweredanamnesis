package com.anamnesis.demo.controller;

import com.anamnesis.demo.serviceinteface.DocumentService;
import org.openapitools.api.DocumentApi;
import org.openapitools.model.DocumentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocumentController implements DocumentApi {

    @Autowired
    private DocumentService documentService;

    @Override
    public ResponseEntity<DocumentDTO> getDocument(String type) {
        return documentService.getDocument(type);
    }

}
