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
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
        
        model.addAttribute("orderList", orderRepository.findAll());
        return "orders";
    }
}
