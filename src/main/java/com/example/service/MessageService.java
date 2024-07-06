package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
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
        if(messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return 1;   // 1 message was deleted
        }
        return 0;   // message not found
    }

    public int updateMessageText(Integer messageId, String messageText) throws InvalidMessageException {
        Message message = messageRepository.findById(messageId).orElse(null);

        // Throw Bad Request if Message is not found
        if (message == null) {
          throw new InvalidMessageException("Invalid message");
        }

        // Throw bad request if messageText is invalid
        if (messageText == null || messageText.isEmpty() || messageText.isBlank() || messageText.length() > 255) {
            throw new InvalidMessageException("Invalid message text");
        }

        message.setMessageText(messageText);
        messageRepository.save(message);
        return 1; // 1 message updated
    }

    public List<Message> findMessagesByAccountId(Integer accountId) {
        Account acc = accountRepository.findById(accountId).orElse(null);
        return messageRepository.findMessagesByPostedBy(acc.getAccountId());
    }
}
