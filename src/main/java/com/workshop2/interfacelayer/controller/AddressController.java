/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2.interfacelayer.controller;

import com.workshop2.domain.Address;
import com.workshop2.domain.Address.AddressType;
import static com.workshop2.domain.Address.AddressType.*;
import com.workshop2.domain.Customer;
import com.workshop2.interfacelayer.repository.AddressRepository;
import com.workshop2.interfacelayer.repository.CustomerRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Ahmed Al-alaaq(Egelantier)
 */
@Controller
@RequestMapping(path = "/addresses")
public class AddressController {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping(path = "/add")
    public String addAddress(Model model) {
        
                
        model.addAttribute(new Address());
        return "addNewAddress";
    }

    @GetMapping
    public String Addresses(Model model) {
        model.addAttribute("addressList", addressRepository.findAll());
        return "addresses";
    }

    @PostMapping(path = "/add")
    public String addNewAccount(@Valid Address address, Errors errors, RedirectAttributes model) {
        if (errors.hasErrors()) {
            model.addAttribute(new Address());
            return "addNewAddress";
        }

        addressRepository.save(address);
        model.addAttribute("addressList", addressRepository.findAll());
        return ("redirect:/addresses");
    }
    
    
    /*@GetMapping(path = "/add")
    public @ResponseBody
    String addNewAddress(
            @RequestParam String streetName,
            @RequestParam int number,
            @RequestParam String addition,
            @RequestParam String postalCode,
            @RequestParam String city,
            //@RequestParam Customer customer,
            @RequestParam Long customerId,
            @RequestParam AddressType addressType) {

        Address address = new Address();
        address.setStreetName(streetName);
        address.setNumber(number);
        address.setAddition(addition);
        address.setPostalCode(postalCode);
        address.setCity(city);
        address.setCustomer(customerRepository.findOne(customerId));
        address.setAddressType(addressType);
        addressRepository.save(address);
        return "Saved";
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Address> getAllAddresses() {
        return addressRepository.findAll();
    }*/

}
