package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAll();
    Optional<Message> findById(Integer messageId);
    void deleteById(Integer messageId);
    int updateMessageText(Integer messageId, String messageText);
    List<Message> findByPostedByAccountId(Integer accountId);
}
