package com.anamnesis.demo.helper;

import com.anamnesis.demo.dao.*;
import org.modelmapper.ModelMapper;
import org.openapitools.model.PatientDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PatientMapper {

    private final ModelMapper modelMapper;

    public PatientMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Patient toPatientEntity(PatientDTO dto) {
        Patient patient = new Patient();
        patient.setPatientId(dto.getPatientId());
        patient.setFullName(dto.getFullName());

        if (dto.getDateOfBirth() != null) {
            patient.setDateOfBirth(java.sql.Date.valueOf(dto.getDateOfBirth())); // LocalDate to java.sql.Date
        }

        patient.setGender(dto.getGender());
        patient.setAddress(dto.getAddress());
        patient.setContactInformation(dto.getContactInformation());
        return patient;
    }

    public MedicalHistory toMedicalHistoryEntity(PatientDTO dto, Patient patient) {
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setPatient(patient); // link the patient

        if (dto.getChronicConditions() != null) {
            medicalHistory.setChronicConditions(dto.getChronicConditions());
        }
        if (dto.getDiagnosisHistory() != null) {
            medicalHistory.setDiagnosisHistory(
                    dto.getDiagnosisHistory().stream()
                            .map(d -> modelMapper.map(d, DiagnosisRecord.class))
                            .collect(Collectors.toList())
            );
        }
        if (dto.getSurgicalHistory() != null) {
            medicalHistory.setSurgicalHistory(
                    dto.getSurgicalHistory().stream()
                            .map(s -> modelMapper.map(s, SurgicalRecord.class))
                            .collect(Collectors.toList())
            );
        }
        if (dto.getAllergies() != null) {
            medicalHistory.setAllergies(
                    dto.getAllergies().stream()
                            .map(a -> modelMapper.map(a, AllergyRecord.class))
                            .collect(Collectors.toList())
            );
        }
        if (dto.getImmunizationStatus() != null) {
            medicalHistory.setImmunizationStatus(
                    dto.getImmunizationStatus().stream()
                            .map(i -> modelMapper.map(i, ImmunizationRecord.class))
                            .collect(Collectors.toList())
            );
        }
        if (dto.getVisitSummaries() != null) {
            medicalHistory.setVisitSummaries(
                    dto.getVisitSummaries().stream()
                            .map(v -> modelMapper.map(v, VisitSummary.class))
                            .collect(Collectors.toList())
            );
        }
        if (dto.getActiveMedications() != null) {
            medicalHistory.setActiveMedications(
                    dto.getActiveMedications().stream()
                            .map(m -> modelMapper.map(m, ActiveMedication.class))
                            .collect(Collectors.toList())
            );
        }
        if (dto.getRecentLabResults() != null) {
            medicalHistory.setRecentLabResults(
                    dto.getRecentLabResults().stream()
                            .map(l -> modelMapper.map(l, LabResult.class))
                            .collect(Collectors.toList())
            );
        }

        return medicalHistory;
    }
}
