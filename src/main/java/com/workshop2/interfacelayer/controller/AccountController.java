/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2.interfacelayer.controller;

import com.workshop2.domain.Account;
import com.workshop2.domain.AccountType;
import com.workshop2.interfacelayer.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author hwkei
 */
@Controller
@RequestMapping(path="/accounts")
public class AccountController {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @GetMapping(path="/add")
    public @ResponseBody String addNewAccount(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam AccountType accountType) {
        
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setAccountType(accountType);
        accountRepository.save(account);
        return "Saved";                
    }
    
    @GetMapping(path="/all")
    public @ResponseBody Iterable<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    
    // Tijdelijke methode voor het maken van een nieuw account, moet nog uitgewerkt worden!
    public Account createAccountForNewCustomer(String firstName, String lastName) {
        Account account = new Account();
        account.setUsername(lastName);
        account.setPassword("welkom"); //AANPASSEN!
        account.setAccountType(AccountType.KLANT);
        return accountRepository.save(account); // Checken of het id klopt in het teruggegeven account!
    }
    
}
