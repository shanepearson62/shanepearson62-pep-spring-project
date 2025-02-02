package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.InvalidAccountException;
import com.example.exception.InvalidMessageException;
import com.example.exception.UnauthorizedException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    
    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    
    // #1 Create a new Account: POST /register. The body will contain a JSON Account, but will not contain an accountId.
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        try {
            Account newAccount = accountService.createAccount(account);
            return new ResponseEntity<>(newAccount, HttpStatus.OK);
        } catch (DuplicateUsernameException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (InvalidAccountException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // #2 Verify a login: POST /login. The request body will contain a JSON representation of an Account.
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        try {
            Account existingAccount = accountService.login(account);
            return new ResponseEntity<>(existingAccount, HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // #3 Submit a new post: POST /messages. The request body will contain a JSON representation of a message, but will not contain a messageId.
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        try {
            Message newMessage = messageService.createMessage(message);
            return new ResponseEntity<>(newMessage, HttpStatus.OK);
        } catch (InvalidMessageException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // #4 Get all messages via a GET request: GET /messages.
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // #5 Get a message by messageId via a GET request: GET /messages/{messageId}.
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Message message = messageService.getMessageById(messageId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // #6 Delete a message by messageId via a DELETE request: DELETE /messages/{messageId}.
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId) {
        int rowsUpdated = messageService.deleteMessageById(messageId);
        if (rowsUpdated > 0) {
            return ResponseEntity.ok(rowsUpdated);
          } else {
            return ResponseEntity.ok().build(); // Return 200 with empty body
        }
    }

    // #7 Update a message via a PATCH request: PATCH /messages/{messageId}. 
    // The request body has new messageText values to replace the message identified by messageId.
    @PatchMapping("/messages/{messageId}") 
    public ResponseEntity<Integer> updateMessageText(@PathVariable Integer messageId, @RequestBody Message message) {
        try {
            int rowsUpdated = messageService.updateMessageText(messageId, message.getMessageText());
            return new ResponseEntity<>(rowsUpdated, HttpStatus.OK);
        } catch (InvalidMessageException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // #8 Get all messages from an accountId via a GET request: GET /accounts/{accountId}/messages.
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer accountId) {
        List<Message> messages = messageService.findMessagesByAccountId(accountId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}