package com.anamnesis.demo.controller;

import com.anamnesis.demo.serviceinteface.MessageService;
import org.openapitools.api.MessageApi;
import org.openapitools.model.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController implements MessageApi {

    @Autowired
    private MessageService messageService;

    @Override
    public ResponseEntity<MessageDTO> createMessage(MessageDTO messageDTO) {
        return messageService.createMessage(messageDTO);
    }

    @Override
    public ResponseEntity<String> deleteMessageById(Integer id) {
        return messageService.deleteMessageById(id);
    }

    @Override
    public ResponseEntity<List<MessageDTO>> getMessagesByChatroomId(Integer chatroomId) {
        return messageService.getMessagesByChatroomId(chatroomId);
    }

    @Override
    public ResponseEntity<List<MessageDTO>> getMessagesByUserId(Integer userId) {
        return messageService.getMessagesByUserId(userId);
    }
}
