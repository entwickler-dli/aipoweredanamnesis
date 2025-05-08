package com.anamnesis.demo.serviceinteface;

import org.openapitools.model.MessageDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MessageService {

    ResponseEntity<MessageDTO> createMessage(MessageDTO messageDTO);

    ResponseEntity<String> deleteMessageById(Integer id);

    ResponseEntity<List<MessageDTO>> getMessagesByChatroomId(Integer chatroomId);

    ResponseEntity<List<MessageDTO>> getMessagesByUserId(Integer userId);

}
