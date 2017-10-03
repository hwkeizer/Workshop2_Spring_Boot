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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    
//    @GetMapping(path="/add")
//    public @ResponseBody String addNewAccount(
//            @RequestParam String username,
//            @RequestParam String password,
//            @RequestParam AccountType accountType) {
//        
//        Account account = new Account();
//        account.setUsername(username);
//        account.setPassword(PasswordHash.generateHash(password));
//        account.setAccountType(accountType);
//        accountRepository.save(account);
//        return "Saved";                
//    }
    
    @GetMapping(path="/add")
    public String showaddAccountForm(Model model) {

        List<AccountType> accountTypeList = new ArrayList<>(Arrays.asList(AccountType.values()));
        model.addAttribute(new Account());        
        model.addAttribute(accountTypeList);
        return "addAccountForm";
    }
    
    @PostMapping(path="/add")
    public String addAccount(@Valid Account account, RedirectAttributes model, Errors errors) {
        if (errors.hasErrors()) {
            return "registerForm";
        }
        account.setPassword(PasswordHash.generateHash(account.getPassword()));
        accountRepository.save(account);
        return("redirect:/accounts");
    }
//    @GetMapping(path="/all")
//    public @ResponseBody Iterable<Account> getAllAccounts() {
//        return accountRepository.findAll();
//    }
    
        @GetMapping
    public String Accounts(Model model) {
        
        model.addAttribute("accountList", accountRepository.findAll());
        return "accounts";
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
