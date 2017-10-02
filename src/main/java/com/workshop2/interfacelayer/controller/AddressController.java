/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2.interfacelayer.controller;

import com.workshop2.domain.Address;
import com.workshop2.domain.Address.AddressType;
import com.workshop2.domain.Customer;
import com.workshop2.interfacelayer.repository.AddressRepository;
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
    public @ResponseBody
    String addNewAddress(
            @RequestParam String streetName,
            @RequestParam int number,
            @RequestParam String addition,
            @RequestParam String postalCode,
            @RequestParam String city,
            @RequestParam Customer customer,
            @RequestParam AddressType addressType) {

        Address address = new Address();
        address.setStreetName(streetName);
        address.setNumber(number);
        address.setAddition(addition);
        address.setPostalCode(postalCode);
        address.setCity(city);
//        if (customerRepository.findOne(customer.getId()) == null) {
//            return null; // tegenwoordig geen address om te maken want er is geen zo customerId
//        }
        address.setCustomer(customerRepository.findOne(customer.getId()));
        address.setAddressType(addressType);
        addressRepository.save(address);
        return "Saved";
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Address> getAllCustomers() {
        return addressRepository.findAll();
    }

    @GetMapping
    public String customers(Model model) {
        
        Customer customer1 = customerRepository.findOne(1L);
        // Tijdelijke code om customertabel even te vullen:
        addNewAddress("Postweg", 201, "h", "3781JK", "Aalst", customer1, AddressType.POSTADRES);
        addNewAddress("Valkstraat", 9, "e", "2424DF", "Goorle", customer1, AddressType.BEZORGADRES);
        model.addAttribute("addressList", addressRepository.findAll());
        return "addresses";
    }

}
