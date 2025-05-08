package com.anamnesis.demo.helper;

import com.anamnesis.demo.dao.*;
import com.anamnesis.demo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private UserRepository userRepository;

    public Mapper() {
        modelMapper.typeMap(User.class, UserDTO.class).addMappings(new PropertyMap<User, UserDTO>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUserId());
                map().setFirstName(source.getFirstName());
                map().setLastName(source.getLastName());
                map().setEmail(source.getEmail());
                map().setPhone(source.getPhone());
                map().setPassword(source.getPassword());
            }
        });

        modelMapper.typeMap(UserDTO.class, User.class).addMappings(new PropertyMap<UserDTO, User>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUserId());
                map().setFirstName(source.getFirstName());
                map().setLastName(source.getLastName());
                map().setEmail(source.getEmail());
                map().setPhone(source.getPhone());
                map().setPassword(source.getPassword());
            }
        });


        modelMapper.typeMap(Document.class, DocumentDTO.class).addMappings(new PropertyMap<Document, DocumentDTO>() {
            @Override
            protected void configure() {
                map().setDescription(source.getDescription());
                map().setLastUpdated(source.getLastUpdated());
                map().setDocumentType(source.getDocumentType());
            }
        });

        modelMapper.typeMap(Patient.class, PatientDTO.class).addMappings(new PropertyMap<Patient, PatientDTO>() {
            @Override
            protected void configure() {
                map().setPatientId(source.getPatientId());
                map().setFullName(source.getFullName());
                map().setDateOfBirth(source.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                map().setGender(source.getGender());
                map().setAddress(source.getAddress());
                map().setContactInformation(source.getContactInformation());
            }
        });

        modelMapper.typeMap(Message.class, MessageDTO.class).addMappings(new PropertyMap<Message, MessageDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setChatroomId(source.getChatroomId());
                map().setUserId(source.getUserId());
                map().setMessage(source.getMessage());
            }
        });

        modelMapper.typeMap(MessageDTO.class, Message.class).addMappings(new PropertyMap<MessageDTO, Message>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setChatroomId(source.getChatroomId());
                map().setUserId(source.getUserId());
                map().setMessage(source.getMessage());
                // Don't map sentAt — it's handled by @PrePersist
            }
        });

    }

    public PatientDTO mapToPatientDTO(Patient patient, MedicalHistory history) {
        PatientDTO dto = modelMapper.map(patient, PatientDTO.class);

        if (history != null) {
            dto.setChronicConditions(history.getChronicConditions());
            dto.setDiagnosisHistory(
                    history.getDiagnosisHistory().stream()
                            .map(d -> modelMapper.map(d, DiagnosisRecordDTO.class))
                            .collect(Collectors.toList())
            );
            dto.setSurgicalHistory(
                    history.getSurgicalHistory().stream()
                            .map(s -> modelMapper.map(s, SurgicalRecordDTO.class))
                            .collect(Collectors.toList())
            );
            dto.setAllergies(
                    history.getAllergies().stream()
                            .map(a -> modelMapper.map(a, AllergyRecordDTO.class))
                            .collect(Collectors.toList())
            );
            dto.setImmunizationStatus(
                    history.getImmunizationStatus().stream()
                            .map(i -> modelMapper.map(i, ImmunizationRecordDTO.class))
                            .collect(Collectors.toList())
            );
            dto.setVisitSummaries(
                    history.getVisitSummaries().stream()
                            .map(v -> modelMapper.map(v, VisitSummaryDTO.class))
                            .collect(Collectors.toList())
            );
            dto.setActiveMedications(
                    history.getActiveMedications().stream()
                            .map(m -> modelMapper.map(m, ActiveMedicationDTO.class))
                            .collect(Collectors.toList())
            );
            dto.setRecentLabResults(
                    history.getRecentLabResults().stream()
                            .map(l -> modelMapper.map(l, LabResultDTO.class))
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    // Helper Methods
    public UserDTO toUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public User toUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public DocumentDTO toDocumentDTO(Document document) {
        return modelMapper.map(document, DocumentDTO.class);
    }

    public MessageDTO toMessageDTO(Message message){
        return modelMapper.map(message, MessageDTO.class);
    }

    public Message toMessage(MessageDTO messageDTO){
        return modelMapper.map(messageDTO, Message.class);
    }

}