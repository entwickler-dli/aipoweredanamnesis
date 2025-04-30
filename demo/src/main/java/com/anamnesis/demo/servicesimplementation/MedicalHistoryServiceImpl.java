package com.anamnesis.demo.servicesimplementation;

import com.anamnesis.demo.dao.*;
import com.anamnesis.demo.helper.Mapper;
import com.anamnesis.demo.helper.PatientMapper;
import com.anamnesis.demo.repository.MedicalHistoryRepository;
import com.anamnesis.demo.repository.PatientRepository;
import com.anamnesis.demo.repository.UserRepository;
import com.anamnesis.demo.serviceinteface.MedicalHistoryService;
import org.modelmapper.ModelMapper;
import org.openapitools.model.PatientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.anamnesis.demo.DemoApplication.log;

@Service
public class MedicalHistoryServiceImpl implements MedicalHistoryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    @Autowired
    private Mapper mapper;

    @Autowired
    private PatientMapper patientMapper;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public ResponseEntity<String> deletePatient(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Patient> patientOptional = patientRepository.findPatientByUser(user);

            if (patientOptional.isPresent()){
                Patient patient = patientOptional.get();
                Optional<MedicalHistory> medicalHistoryOptional = medicalHistoryRepository.findByPatient(patient);

                if (medicalHistoryOptional.isPresent()){
                    MedicalHistory medicalHistory = medicalHistoryOptional.get();

                    medicalHistoryRepository.delete(medicalHistory);
                    patientRepository.delete(patient);
                    log.info("Patient deleted:" + patient.getPatientId());
                    return ResponseEntity.status(HttpStatus.OK).build();
                }
            }
        }
        log.info("Patient could not be deleted");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<PatientDTO> getPatientById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Patient> patientOptional = patientRepository.findPatientByUser(user);

            if (patientOptional.isPresent()){
                Patient patient = patientOptional.get();
                Optional<MedicalHistory> medicalHistoryOptional = medicalHistoryRepository.findByPatient(patient);

                if (medicalHistoryOptional.isPresent()){
                    MedicalHistory medicalHistory = medicalHistoryOptional.get();

                    PatientDTO patientDTO = mapper.mapToPatientDTO(patient, medicalHistory);

                    log.info("Patient requested:" + patient.getPatientId());
                    return ResponseEntity.ok(patientDTO);
                }
            }
        }
        log.info("Patient could not be returned");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<String> registerPatient(Integer userId, PatientDTO patientDTO) {
        if (patientRepository.existsById(patientDTO.getPatientId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Patient already exists");
        }

        Patient patient = patientMapper.toPatientEntity(patientDTO);
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            LocalDateTime currentDateTime = LocalDateTime.now();
            Date sqlDate = Date.valueOf(currentDateTime.toLocalDate());

            patient.setUser(user);
            patient.setCreatedAt(sqlDate);
            patient.setUpdatedAt(sqlDate);
            patientRepository.save(patient);

            MedicalHistory medicalHistory = patientMapper.toMedicalHistoryEntity(patientDTO, patient);
            medicalHistory.setCreatedAt(sqlDate);
            medicalHistory.setUpdatedAt(sqlDate);
            medicalHistoryRepository.save(medicalHistory);

            log.info("Patient created with medical history:" + patient.getPatientId());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        log.info("Patient could not be created");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Override
    public ResponseEntity<String> updatePatient(Integer id, PatientDTO patientDTO) {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isEmpty()) {
            log.info("User not found for update");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = existingUser.get();

        Optional<Patient> patientOptional = patientRepository.findPatientByUser(user);
        if (patientOptional.isPresent()){
            Patient patient = patientOptional.get();

            Optional<MedicalHistory> historyOptional = medicalHistoryRepository.findByPatient(patient);
            
            if (historyOptional.isPresent()){
                MedicalHistory history = historyOptional.get();

                // Update Patient fields
                if (patientDTO.getFullName() != null) {
                    patient.setFullName(patientDTO.getFullName());
                }
                if (patientDTO.getDateOfBirth() != null) {
                    patient.setDateOfBirth(java.sql.Date.valueOf(patientDTO.getDateOfBirth()));
                }
                if (patientDTO.getGender() != null) {
                    patient.setGender(patientDTO.getGender());
                }
                if (patientDTO.getAddress() != null) {
                    patient.setAddress(patientDTO.getAddress());
                }
                if (patientDTO.getContactInformation() != null) {
                    patient.setContactInformation(patientDTO.getContactInformation());
                }

                // Update MedicalHistory fields
                if (patientDTO.getChronicConditions() != null) {
                    history.setChronicConditions(patientDTO.getChronicConditions());
                }
                if (patientDTO.getDiagnosisHistory() != null) {
                    history.setDiagnosisHistory(patientDTO.getDiagnosisHistory().stream()
                            .map(dto -> modelMapper.map(dto, DiagnosisRecord.class))
                            .collect(Collectors.toList()));
                }
                if (patientDTO.getSurgicalHistory() != null) {
                    history.setSurgicalHistory(patientDTO.getSurgicalHistory().stream()
                            .map(dto -> modelMapper.map(dto, SurgicalRecord.class))
                            .collect(Collectors.toList()));
                }
                if (patientDTO.getAllergies() != null) {
                    history.setAllergies(patientDTO.getAllergies().stream()
                            .map(dto -> modelMapper.map(dto, AllergyRecord.class))
                            .collect(Collectors.toList()));
                }
                if (patientDTO.getImmunizationStatus() != null) {
                    history.setImmunizationStatus(patientDTO.getImmunizationStatus().stream()
                            .map(dto -> modelMapper.map(dto, ImmunizationRecord.class))
                            .collect(Collectors.toList()));
                }
                if (patientDTO.getVisitSummaries() != null) {
                    history.setVisitSummaries(patientDTO.getVisitSummaries().stream()
                            .map(dto -> modelMapper.map(dto, VisitSummary.class))
                            .collect(Collectors.toList()));
                }
                if (patientDTO.getActiveMedications() != null) {
                    history.setActiveMedications(patientDTO.getActiveMedications().stream()
                            .map(dto -> modelMapper.map(dto, ActiveMedication.class))
                            .collect(Collectors.toList()));
                }
                if (patientDTO.getRecentLabResults() != null) {
                    history.setRecentLabResults(patientDTO.getRecentLabResults().stream()
                            .map(dto -> modelMapper.map(dto, LabResult.class))
                            .collect(Collectors.toList()));
                }

                patientRepository.save(patient);
                medicalHistoryRepository.save(history);

                log.info("Patient and Medical History was updated");
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        }

        log.info("Patient not found for update");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
