package com.anamnesis.demo.repository;

import com.anamnesis.demo.dao.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    Optional<List<Message>> findByChatroomId(Integer chatroomId);

    Optional<List<Message>> findByUserId(Integer userId);
}
