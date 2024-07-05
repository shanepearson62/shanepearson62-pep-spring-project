package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.InvalidMessageException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    public Message createMessage(Message message) throws InvalidMessageException {
        if (message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            throw new InvalidMessageException("Invalid message text");
        }
        if (message.getPostedBy() == null || !accountRepository.existsById((long)message.getPostedBy())) {
            throw new InvalidMessageException("Posted by user does not exist");
        }
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}
