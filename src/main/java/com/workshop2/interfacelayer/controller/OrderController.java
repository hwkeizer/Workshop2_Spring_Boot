/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2.interfacelayer.controller;

import com.workshop2.domain.Order;
import com.workshop2.domain.OrderStatus;
import static com.workshop2.domain.OrderStatus.*;
import com.workshop2.interfacelayer.repository.OrderItemRepository;
import com.workshop2.interfacelayer.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

/**
 *
 * @author thoma
 */
@Controller
@RequestMapping(path="/orders")
public class OrderController {
    
    @Autowired
    OrderItemRepository orderItemRepository;
    
    @Autowired
    OrderRepository orderRepository;
    
    @GetMapping(path="/add")
    public @ResponseBody String addNewOrder(
            @RequestParam BigDecimal totalPrice,
            @RequestParam LocalDateTime dateTime,
            @RequestParam OrderStatus orderStatus) {
        
       Order order = new Order();
       order.setTotalPrice(totalPrice);
       order.setDateTime(dateTime);
       order.setOrderStatus(orderStatus);
        System.out.println(order);
       orderRepository.save(order);
       return "Saved";
    } 
    
    @GetMapping(path="/all")
    public @ResponseBody Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    @GetMapping
    public String orders(Model model) {
        
        // Tijdelijke code om customertabel even te vullen:
        addNewOrder(new BigDecimal("10.00"), LocalDateTime.now(), NIEUW);
        addNewOrder(new BigDecimal("20.00"), LocalDateTime.now(), IN_BEHANDELING);
        addNewOrder(new BigDecimal("40.00"), LocalDateTime.now(), AFGEHANDELD);
        
        model.addAttribute("orderList", orderRepository.findAll());
        return "orders";
    }
}
