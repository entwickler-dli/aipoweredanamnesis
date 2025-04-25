package com.anamnesis.demo.helper;

import com.anamnesis.demo.dao.*;
import com.anamnesis.demo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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

}