package com.anamnesis.demo.controller;

import com.anamnesis.demo.serviceinteface.MedicalHistoryService;
import org.openapitools.api.PatientApi;
import org.openapitools.model.PatientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MedicalHistoryController implements PatientApi {

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @Override
    public ResponseEntity<String> deletePatient(Integer id) {
        return medicalHistoryService.deletePatient(id);
    }

    @Override
    public ResponseEntity<PatientDTO> getPatientById(Integer id) {
        return medicalHistoryService.getPatientById(id);
    }

    @Override
    public ResponseEntity<String> registerPatient(Integer userId, PatientDTO patientDTO) {
        return medicalHistoryService.registerPatient(userId, patientDTO);
    }

    @Override
    public ResponseEntity<String> updatePatient(Integer id, PatientDTO patientDTO) {
        return medicalHistoryService.updatePatient(id, patientDTO);
    }
}
