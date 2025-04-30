package com.anamnesis.demo.controller;

import org.openapitools.api.MessageApi;
import org.openapitools.model.MessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController implements MessageApi {
    @Override
    public ResponseEntity<MessageDTO> createMessage(MessageDTO messageDTO) {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteMessageById(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<List<MessageDTO>> getMessagesByChatroomId(Integer chatroomId) {
        return null;
    }

    @Override
    public ResponseEntity<List<MessageDTO>> getMessagesByUserId(Integer userId) {
        return null;
    }
}
