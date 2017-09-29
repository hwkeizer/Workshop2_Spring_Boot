/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2.interfacelayer.controller;

import com.workshop2.domain.Customer;
import com.workshop2.interfacelayer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author hwkei
 */
@Controller
@RequestMapping(path="/customers")
public class CustomerController {
    
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountController accountController;
    
    @GetMapping(path="/add")
    public @ResponseBody String addNewCustomer(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String lastNamePrefix) {
        
       Customer customer = new Customer();
       customer.setFirstName(firstName);
       customer.setLastName(lastName);
       customer.setLastNamePrefix(lastNamePrefix);
       customer.setAccount(accountController.createAccountForNewCustomer(firstName, lastName));
       customerRepository.save(customer);
       return "Saved";
    } 
    
    @GetMapping(path="/all")
    public @ResponseBody Iterable<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    @GetMapping
    public String customers(Model model) {
        
        // Tijdelijke code om customertabel even te vullen:
        addNewCustomer("Piet", "Pietersen", null);
        addNewCustomer("Klaas", "Klaassen", null);
        addNewCustomer("Fred", "Wal", "van der");
        
        model.addAttribute("customerList", customerRepository.findAll());
        return "customers";
    }
    
    // Methode om rechtstreeks een customer aan te maken. Handig/Nodig....????
//    public void createCustomer(String firstName, String lastName, String lastNamePrefix) {
//        Customer customer = new Customer();
//        customer.setFirstName(firstName);
//        customer.setLastName(lastName);
//        customer.setLastNamePrefix(lastNamePrefix);
//        customer.setAccount(accountController.createAccountForNewCustomer(firstName, lastName));
//        customerRepository.save(customer);
//    }
}
