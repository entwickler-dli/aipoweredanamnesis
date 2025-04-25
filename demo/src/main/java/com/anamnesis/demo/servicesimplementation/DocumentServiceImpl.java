package com.anamnesis.demo.servicesimplementation;

import  com.anamnesis.demo.helper.Mapper;
import com.anamnesis.demo.repository.DocumentRepository;
import  com.anamnesis.demo.serviceinteface.DocumentService;
import org.openapitools.model.DocumentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.anamnesis.demo.DemoApplication.log;

@Service
public class DocumentServiceImpl implements DocumentService{

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private Mapper mapper;

    @Override
    public ResponseEntity<DocumentDTO> getDocument(String type) {

        if(!documentRepository.existsByDocumentType(type)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        DocumentDTO documentDTO = mapper.toDocumentDTO(documentRepository.findByDocumentType(type).get());;

        log.info("Document generated:" + documentDTO.getDocumentType());
        return ResponseEntity.ok(documentDTO);
    }

}
