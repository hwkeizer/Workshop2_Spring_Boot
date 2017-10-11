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
import static jdk.nashorn.internal.runtime.Debug.id;
import static org.apache.tomcat.jni.Buffer.address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @Autowired
    private CustomerController customerController;

    @GetMapping
    public String Addresses(Model model) {
        model.addAttribute("addressList", addressRepository.findAll());
        return "address/addresses";
    }

    @GetMapping(path = "/add")
    public String addAddress(Model model) {
        List<AddressType> addressTypeList = new ArrayList<>(Arrays.asList(AddressType.values()));
        List allCustomerId = getAllCustomerId();
        model.addAttribute(new Address());
        model.addAttribute("customerIdList", allCustomerId);
        model.addAttribute("addressTypeList", addressTypeList);
        return "address/addNewAddress";
    }

    @PostMapping(path = "/add")
    public String addNewAddress(@Valid Address address, Errors errors, RedirectAttributes model) {
        if (errors.hasErrors()) {
            List<AddressType> addressTypeList = new ArrayList<>(Arrays.asList(AddressType.values()));
            List allCustomerId = getAllCustomerId();
            model.addAttribute("customerIdList", allCustomerId);
            model.addAttribute("addressTypeList", addressTypeList);
            return "address/addNewAddress";
        }
        Customer customer = customerRepository.findOne(address.getCustomer().getId());
        address.setCustomer(customer);
        addressRepository.save(address);
        model.addAttribute("addressList", addressRepository.findAll());
        return ("redirect:/addresses");
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String AddressToDelete(@PathVariable long id, Model model) {
        model.addAttribute(addressRepository.findOne(id));
        return "address/deleteAddress";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteAddress(@Valid Address address, Model model) {
        addressRepository.delete(address);
        return ("redirect:/addresses");
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String AddressToUpdate(@PathVariable long id, Model model) {
        List<AddressType> addressTypeList = new ArrayList<>(Arrays.asList(AddressType.values()));
        model.addAttribute(addressRepository.findOne(id));
        model.addAttribute("addressTypeList", addressTypeList);
        return "address/updateAddress";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateAddress(Address address, BindingResult result, Model model) {

        if (result.hasErrors()) {
            List<AddressType> addressTypeList = new ArrayList<>(Arrays.asList(AddressType.values()));
            model.addAttribute("addressTypeList", addressTypeList);
            return "address/updateAddress";
        }
        addressRepository.save(address);
        return ("redirect:/addresses");
    }
    
    public List getAllCustomerId(){
        List<Customer> customers = customerRepository.findAll();
        List allId = new ArrayList <>();
        for (Customer customer : customers)
        allId.add(customer.getId());
        return allId;
    }

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

 */
