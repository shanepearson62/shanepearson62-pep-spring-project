package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.InvalidAccountException;
import com.example.exception.UnauthorizedException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    public Account createAccount(Account account) throws DuplicateUsernameException, InvalidAccountException {
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            throw new InvalidAccountException("Invalid account details");
        }
        if (accountRepository.findAccountByUsername(account.getUsername()).isPresent()) {
            throw new DuplicateUsernameException("Username already exists");
        }
        return accountRepository.save(account);
    }

    public Account login(Account account) throws UnauthorizedException {
        Optional<Account> existingAccount = accountRepository.findAccountByUsernameAndPassword(account.getUsername(), account.getPassword());
        if (existingAccount.isPresent()) {
            return existingAccount.get();
        } else {
            throw new UnauthorizedException("Invalid username or password");
        }
    }
}