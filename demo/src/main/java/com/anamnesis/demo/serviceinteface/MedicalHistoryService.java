package com.anamnesis.demo.serviceinteface;

import org.openapitools.model.PatientDTO;
import org.springframework.http.ResponseEntity;

public interface MedicalHistoryService {

    ResponseEntity<String> deletePatient(Integer id);

    ResponseEntity<PatientDTO> getPatientById(Integer id);

    ResponseEntity<String> registerPatient(Integer userId, PatientDTO patientDTO);

    ResponseEntity<String> updatePatient(Integer id, PatientDTO patientDTO);

}
