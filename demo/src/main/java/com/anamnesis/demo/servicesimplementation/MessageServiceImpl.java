package com.anamnesis.demo.servicesimplementation;

import com.anamnesis.demo.dao.ImmunizationRecord;
import com.anamnesis.demo.dao.Message;
import com.anamnesis.demo.helper.Mapper;
import com.anamnesis.demo.repository.MessageRepository;
import com.anamnesis.demo.serviceinteface.MessageService;
import org.openapitools.model.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.anamnesis.demo.DemoApplication.log;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private Mapper mapper;

    @Override
    public ResponseEntity<MessageDTO> createMessage(MessageDTO messageDTO) {
        Message message = mapper.toMessage(messageDTO);
        messageRepository.save(message);
        log.info("Message created:" + message.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<String> deleteMessageById(Integer id) {
        Optional<Message> messageOptional = messageRepository.findById(id);
        if (messageOptional.isPresent()){
            Message message = messageOptional.get();
            messageRepository.delete(message);

            log.info("Message deleted:" + message.getId());
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        log.info("Message could not be deleted");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<List<MessageDTO>> getMessagesByChatroomId(Integer chatroomId) {
        Optional<List<Message>> messageOptionalList = messageRepository.findByChatroomId(chatroomId);
        if (messageOptionalList.isPresent()){
            List<Message> messages = messageOptionalList.get();

            List<MessageDTO> messageDTO = messages.stream()
                    .map(message -> mapper.toMessageDTO(message))
                    .collect(Collectors.toList());

            log.info("Message returned");
            return ResponseEntity.status(HttpStatus.OK).body(messageDTO);
        }
        log.info("Messages not found based on chatroomId");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<List<MessageDTO>> getMessagesByUserId(Integer userId) {
        Optional<List<Message>> messageOptionalList = messageRepository.findByUserId(userId);
        if (messageOptionalList.isPresent()){
            List<Message> messages = messageOptionalList.get();

            List<MessageDTO> messageDTO = messages.stream()
                    .map(message -> mapper.toMessageDTO(message))
                    .collect(Collectors.toList());

            log.info("Message returned");
            return ResponseEntity.status(HttpStatus.OK).body(messageDTO);
        }
        log.info("Messages not found based on chatroomId");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
