/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2.interfacelayer.controller;

import com.workshop2.domain.Customer;
import com.workshop2.interfacelayer.repository.AccountRepository;
import com.workshop2.interfacelayer.repository.CustomerRepository;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private AccountRepository accountRepository;
    @Autowired
    private AccountController accountController;
    
    @GetMapping(path="/add")
    public String showaddAccountForm(Model model) {
        model.addAttribute(new Customer());        
        return "customer/addCustomerForm";
    }
    
    @PostMapping(path="/add")
    public String addCustomer(@Valid Customer customer, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "customer/addCustomerForm";
        }
        // Add a default account for the new customer
        customer.setAccount(accountController.createAccountForNewCustomer(customer.getFirstName(),
                customer.getLastName()));
        System.out.println(customerRepository.save(customer));
        return("redirect:/customers");
    }
    
    @GetMapping(path="/all")
    public @ResponseBody Iterable<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    @GetMapping
    public String customers(Model model) {        
        model.addAttribute("customerList", customerRepository.findAll());
        return "customer/customers";
    }
    
    public List<Customer> findCustomerByAccountId(Long accountId) {
        Customer customer = new Customer();
        customer.setAccount(accountRepository.findOne(accountId));
        Example<Customer> example = Example.of(customer);
        return customerRepository.findAll(example);        
    }
    
}
