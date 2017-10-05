/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2.interfacelayer.controller;

import com.workshop2.domain.Account;
import com.workshop2.domain.AccountType;
import com.workshop2.interfacelayer.repository.AccountRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author hwkei
 */
@Controller
@RequestMapping(path="/accounts")
public class AccountController {
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
    
    @Autowired
    private AccountRepository accountRepository;
    
    @GetMapping(path="/add")
    public String showaddAccountForm(Model model) {
        List<AccountType> accountTypeList = new ArrayList<>(Arrays.asList(AccountType.values()));
        model.addAttribute(new Account());        
        model.addAttribute(accountTypeList);
        return "account/addAccountForm";
    }
    
    @PostMapping(path="/add")
    public String addAccount(@Valid Account account, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<AccountType> accountTypeList = new ArrayList<>(Arrays.asList(AccountType.values()));
            model.addAttribute(accountTypeList);
            return "account/addAccountForm";
        }
        account.setPassword(PasswordHash.generateHash(account.getPassword()));
        try {
            accountRepository.save(account);
            return("redirect:/accounts");
        } catch(DataIntegrityViolationException e) {
            return("account/err_duplicate_account");
        }
    }

    @GetMapping
    public String Accounts(Model model) {        
        model.addAttribute("accountList", accountRepository.findAll());
        return "account/accounts";
    }
      
    public boolean validateAccount(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        List<Account> resultList = accountRepository.findAll(Example.of(account));
        if (resultList != null && resultList.size() == 1) {
            return PasswordHash.validatePassword(password, resultList.get(0).getPassword());
        } else {
            log.debug("Validation user {} failed", username);
            return false; // Account not found
        }
    }
    
    // 
    public AccountType getUserRole(String username) {
        Account account = new Account();
        account.setUsername(username);
        List<Account> resultList = accountRepository.findAll(Example.of(account));
        if (resultList != null && resultList.size() == 1) {
            return resultList.get(0).getAccountType();
        } else {
            return null;
        }
    }
    
    // Tijdelijke methode voor het maken van een nieuw account, moet nog uitgewerkt worden!
    public Account createAccountForNewCustomer(String firstName, String lastName) {
        Account account = new Account();
        account.setUsername(lastName);
        account.setPassword(PasswordHash.generateHash("welkom")); //AANPASSEN!
        account.setAccountType(AccountType.KLANT);
        return accountRepository.save(account); // Checken of het id klopt in het teruggegeven account!
    }
    
}
