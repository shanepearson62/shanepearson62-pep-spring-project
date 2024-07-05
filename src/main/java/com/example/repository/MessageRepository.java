package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllMessages();
    Optional<Message> findMessagesById(Integer messageId);
    void deleteMessagesById(Integer messageId);
    int updateMessageText(Integer messageId, String messageText);
    List<Message> findMessagesByPostedByAccountId(Integer accountId);
}
