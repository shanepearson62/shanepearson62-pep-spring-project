package com.example.service;

import java.util.List;
import java.util.Optional;

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
        if (message.getPostedBy() == null || !accountRepository.existsById(message.getPostedBy())) {
            throw new InvalidMessageException("Posted by user does not exist");
        }
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer messageId) {
        Optional<Message> messageOptional = messageRepository.findById(messageId);
        return messageOptional.orElse(null);
    }

    public int deleteMessageById(Integer messageId) {
        messageRepository.deleteById(messageId);
        if(messageRepository.existsById(messageId)) {
            return 0;
        }
        return 1;
    }

    /*public int updateMessageText(Integer messageId, String messageText) throws InvalidMessageException {
        if (messageText == null || messageText.isBlank() || messageText.length() > 255) {
            throw new InvalidMessageException("Invalid message text");
        }
        return messageRepository.updateMessage(messageId, messageText);
    }

    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.getMessagesByAccountId(accountId);
    }*/
}
