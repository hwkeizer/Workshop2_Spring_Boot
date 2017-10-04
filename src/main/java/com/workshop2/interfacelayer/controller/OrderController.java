/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2.interfacelayer.controller;

import com.workshop2.domain.Customer;
import com.workshop2.domain.Order;
import com.workshop2.domain.OrderItem;
import com.workshop2.domain.OrderStatus;
import static com.workshop2.domain.OrderStatus.*;
import com.workshop2.domain.Product;
import com.workshop2.interfacelayer.repository.CustomerRepository;
import com.workshop2.interfacelayer.repository.OrderItemRepository;
import com.workshop2.interfacelayer.repository.OrderRepository;
import com.workshop2.interfacelayer.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author thoma
 */
@Controller
@RequestMapping(path="/orders")
public class OrderController {
    
    @Scope(value = "session",proxyMode = ScopedProxyMode.TARGET_CLASS)
    private class ScopedOrder extends Order {
        
    }
    
    private ScopedOrder order;
    
    @Autowired
    CustomerRepository customerRepository;
    
    @Autowired
    OrderItemRepository orderItemRepository;
    
    @Autowired
    OrderRepository orderRepository;
    
    @Autowired
    ProductRepository productRepository;
    
    
    @GetMapping
    public String orders(Model model) {

        return "order_menu";
    }
    
    @GetMapping(path="/startneworder")
    public String startAddNewOrder(Model model) {
        order = new ScopedOrder();
       
        model.addAttribute("order", order);
        return "order/addOrder";
    }
    
    @GetMapping(path="/addorder")
    public String addNewOrder(Model model) {
        System.out.println("ORDER BEFORE ACCESSING ORDER OVERVIEW");
        System.out.println(order.toStringNoId());
        System.out.println("ADDED ORDERITEM BEFORE ACCESSING ORDER OVERVIEW");
        System.out.println(order.getOrderItemList().get(0).toStringNoId());
        
        model.addAttribute("order", order);
       return "order/addOrder";
    }
    
    @GetMapping(path="/saveorder")
    public String saveNewOrder(Model model) {
        Order orderToSave = new Order();
        orderToSave.setCustomer(order.getCustomer());
        orderToSave.setOrderStatus(NIEUW);
        orderToSave.setDateTime(LocalDateTime.now());
        orderToSave.setTotalPrice(order.getTotalPrice());
        orderToSave.setOrderItemList(order.getOrderItemList());
        
        for(OrderItem orderItem: orderToSave.getOrderItemList()) {
            orderItemRepository.save(orderItem);
        }
        orderRepository.save(orderToSave);
        
        return "redirect:/orders";
    }
    
    @GetMapping(path="/addorderitem")
    public String addNewOrderItem(Model model) {
        List<Product> productList = productRepository.findAll();
        model.addAttribute(new OrderItem());
        model.addAttribute(productList);
        String productName = null;
        model.addAttribute("productName", productName);
        
        return "order/addNewOrderItem";
    }
    
    @PostMapping(path="/addorderitem")
    public String addNewOrderItem(@Valid OrderItem orderItem, BindingResult bindingResult, Model model) {
        System.out.println("POST METHOD WAS REACHED!");
        
        if(bindingResult.hasErrors()) {
            List<Product> productList = productRepository.findAll();
            model.addAttribute(productList);
        }
        
        // fake product for testing
        List<Product> productList = productRepository.findAll();
        Product product = productList.get(0);
        orderItem.setProduct(product);
        BigDecimal subTotal = product.getPrice().multiply(new BigDecimal(orderItem.getAmount()));
        orderItem.setSubTotal(subTotal);
        
        // add the orderItem to the Order
        order.addToOrderItemList(orderItem);
        
        //fake customer for testing
        List<Customer> customerList = customerRepository.findAll();
        Customer customer = customerList.get(2);
        order.setCustomer(customer);
        
        // set totalPrice
        order.setTotalPrice(calculateOrderPrice(order.getOrderItemList()));
        
        // set orderStatus
        order.setOrderStatus(NIEUW);
        
        // order.setDate
        order.setDateTime(LocalDateTime.now());
        
        
        System.out.println("REACHED JUST BEFORE ORDER PRINT");
        System.out.println("ORDER IS: ");
        System.out.println(order.toStringNoId());
        System.out.println(order.getOrderItemList().get(0).toStringNoId());

        return("redirect:/orders/addorder");
    }

    @GetMapping(path="/all_orders")
    public String showOrdersAll(Model model) {
        
        model.addAttribute("orderList", orderRepository.findAll());
        return "order/orders_all";
    }
    
    @GetMapping(path="/orderstatus_new")
    public String showOrdersNew(Model model) {
        List<Order> orderList = orderRepository.findByOrderStatus(NIEUW);

        model.addAttribute("orderList", orderList);
        return "order/orders_new";
    }
    
    @GetMapping(path="/orderstatus_inprogress")
    public String showOrdersInProgress(Model model) {
        List<Order> orderList = orderRepository.findByOrderStatus(IN_BEHANDELING);

        model.addAttribute("orderList", orderList);
        return "order/orders_inprogress";
    }
    
    @GetMapping(path="/orderstatus_finished")
    public String showOrdersFinished(Model model) {
        List<Order> orderList = orderRepository.findByOrderStatus(AFGEHANDELD);
        
        model.addAttribute("orderList", orderList);
        return "order/orders_finished";
    }
    
    @GetMapping(path="/show1order")
    public String showOneOrder(Model model) {
        List<Order> orderList = orderRepository.findAll();
        Order order = orderList.get(0);
        model.addAttribute("order", order);
        return "order/show1order";
    }
    
    protected BigDecimal calculateOrderPrice(List<OrderItem> orderItemList) {
        BigDecimal price = new BigDecimal("0.00");
        for(OrderItem orderItem: orderItemList){
            price = price.add(orderItem.getSubTotal());

        }
        return price;
    }
}
