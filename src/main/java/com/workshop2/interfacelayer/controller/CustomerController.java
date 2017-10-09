/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2.interfacelayer.controller;

import com.workshop2.domain.Account;
import com.workshop2.domain.Address;
import com.workshop2.domain.Address.AddressType;
import com.workshop2.domain.Customer;
import com.workshop2.domain.Order;
import com.workshop2.domain.OrderStatus;
import com.workshop2.interfacelayer.repository.AccountRepository;
import com.workshop2.interfacelayer.repository.AddressRepository;
import com.workshop2.interfacelayer.repository.CustomerRepository;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author hwkei
 */
@Controller
@RequestMapping(path = "/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AccountController accountController;

    @GetMapping(path = "/add")
    public String showaddAccountForm(Model model) {
        model.addAttribute(new Customer());
        return "customer/addCustomerForm";
    }

    @PostMapping(path = "/add")
    public String addCustomer(@Valid Customer customer, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "customer/addCustomerForm";
        }
        // Add a default account for the new customer
        customer.setAccount(accountController.createAccountForNewCustomer(customer.getFirstName(),
                customer.getLastName()));
        System.out.println(customerRepository.save(customer));
        return ("redirect:/customers");
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String showEditCustomer(@PathVariable Long id, Model model) {
        model.addAttribute(customerRepository.findOne(id));
        return "customer/editCustomerForm";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editCustomer(Customer customer, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "customer/editCustomerForm";
        }
        customerRepository.save(customer);
        return "redirect:/customers";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String showDeleteCustomer(@PathVariable Long id, Model model) {
        Customer customer = customerRepository.findOne(id);
        List<Address> addressList = customer.getAddressList();
        for (Address address : addressList) {
            switch (address.getAddressType()) {
                case POSTADRES:
                    model.addAttribute("postadres", address);
                    break;
                case BEZORGADRES:
                    model.addAttribute("bezorgadres", address);
                    break;
                case FACTUURADRES:
                    model.addAttribute("factuuradres", address);
                    break;
            }
        }
        model.addAttribute("orderList", customer.getOrderList());
        model.addAttribute(customer);
        return "customer/delete_customer";
    }

    @GetMapping(value = "/deleteconfirm/{id}")
    public String deleteCustomerExecution(@PathVariable Long id, Model model) {
        Customer customer = customerRepository.findOne(id);

        Account account = customer.getAccount();

        // Set customer property from orders from this customer to null
        for (Order order : customer.getOrderList()) {
            order.setCustomer(null);
            order.setOrderStatus(OrderStatus.AFGEHANDELD);
        }

        // Delete addresses from this customer
        for (Address address : customer.getAddressList()) {
            addressRepository.delete(address);
        }

        customerRepository.delete(customer);
        accountRepository.delete(account);

        return "redirect:/customers";

    }

//    @RequestMapping(value="/delete", method=RequestMethod.POST)
//    public String deleteCustomer(Customer customer, BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            return "customer/deleteCustomerForm";
//        }
//        customerRepository.delete(customer);
//        return "redirect:/customers";
//    }
    @RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
    public String showCustomerDetails(@PathVariable Long id, Model model) {
        Customer customer = customerRepository.findOne(id);
        List<Address> addressList = customer.getAddressList();
        for (Address address : addressList) {
            switch (address.getAddressType()) {
                case POSTADRES:
                    model.addAttribute("postadres", address);
                    break;
                case BEZORGADRES:
                    model.addAttribute("bezorgadres", address);
                    break;
                case FACTUURADRES:
                    model.addAttribute("factuuradres", address);
                    break;
            }
        }
        model.addAttribute(customer);
        return "customer/details";
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Customer> getAllCustomers() {
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
