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
import org.springframework.data.domain.Example;
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

        return "order/order_menu";
    }
    
    @GetMapping(path="/startaddneworder")
    public String startAddNewOrder(Model model) {
        order = new ScopedOrder();
       
        model.addAttribute("order", order);
        return "order/addOrder_start";
    }
    
    @GetMapping(path="/addorder")
    public String addNewOrder(Model model) {
        List<Product> productList = productRepository.findAll();
        
        // Only send products which have not yet been chosen
        cleanProductList(productList, order.getOrderItemList());
        model.addAttribute(new OrderItem());
        model.addAttribute("productList", productList);
        
        model.addAttribute("order", order);
        if(productList.size() != 0)    
            return "order/addOrder";
        else
            return "order/addOrder_noMoreProducts";
    }
    
    @GetMapping(path="/saveorder")
    public String saveNewOrder(Model model) {
        Order orderToSave = new Order();
        orderToSave.setCustomer(order.getCustomer());
        orderToSave.setOrderStatus(NIEUW);
        orderToSave.setDateTime(LocalDateTime.now());
        orderToSave.setTotalPrice(order.getTotalPrice());
        orderToSave.setOrderItemList(order.getOrderItemList());
        
        updateProductStockAfterCreatingOrder(order.getOrderItemList());
        
        for(OrderItem orderItem: orderToSave.getOrderItemList()) {
            orderItemRepository.save(orderItem);
        }
        orderRepository.save(orderToSave);
        
        return "redirect:/orders";
    }
    
    @GetMapping(path="/addorderitem")
    public String addNewOrderItem(Model model) {
        List<Product> productList = productRepository.findAll();
        
        // Only send products which have not yet been chosen
        cleanProductList(productList, order.getOrderItemList());
        model.addAttribute(new OrderItem());
        model.addAttribute("productList", productList);
        
        return "order/addNewOrderItem";
    }
    
    @PostMapping(path="/addorderitem")
    public String addNewOrderItem(@Valid OrderItem orderItem, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            List<Product> productList = productRepository.findAll();
        
            // Only send products which have not yet been chosen
            cleanProductList(productList, order.getOrderItemList());
            model.addAttribute(new OrderItem());
            model.addAttribute("productList", productList);

            return "order/addNewOrderItem";
        }
        if(orderItem.getAmount() == 0) {
            // order not empty, go back to overview order so far
            System.out.println("ORDERITEMLIST SIZE: " + order.getOrderItemList().size());
            if(order.getOrderItemList().size() != 0)
                return ("redirect:/orders/addorder");
            else
            // entered amount = 0 on first product, so returning to startAddNewOrder    
                return("redirect:/orders/startaddneworder");
        }
        
        // get the product
        Long id = orderItem.getProduct().getId();
        Product product = productRepository.findOne(id);
        orderItem.setProduct(product);
        
        // calculate and set subtotal
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

        return("redirect:/orders/addorder");
    }

    @GetMapping(path="/all_orders")
    public String showOrdersAll(Model model) {
        
        model.addAttribute("orderList", orderRepository.findAll());
        return "order/showorders_all";
    }
    
    @GetMapping(path="/orderstatus_new")
    public String showOrdersNew(Model model) {
        List<Order> orderList = orderRepository.findByOrderStatus(NIEUW);

        model.addAttribute("orderList", orderList);
        return "order/showorders_new";
    }
    
    @GetMapping(path="/orderstatus_inprogress")
    public String showOrdersInProgress(Model model) {
        List<Order> orderList = orderRepository.findByOrderStatus(IN_BEHANDELING);

        model.addAttribute("orderList", orderList);
        return "order/showorders_inprogress";
    }
    
    @GetMapping(path="/orderstatus_finished")
    public String showOrdersFinished(Model model) {
        List<Order> orderList = orderRepository.findByOrderStatus(AFGEHANDELD);
        
        model.addAttribute("orderList", orderList);
        return "order/showorders_finished";
    }
    
    @GetMapping(value="/details/{id}")
    public String showOrderDetails(@PathVariable Long id, Model model) {
        Order order = orderRepository.findOne(id);
        model.addAttribute(order); 
        return "order/show1order_details";
    }
    
    @GetMapping(value="/delete/{id}")
    public String showDeleteOrder(@PathVariable Long id, Model model) {
        Order order = orderRepository.findOne(id);
        model.addAttribute(order);
        model.addAttribute("orderId", order.getId());
        if(order.getOrderStatus().equals(OrderStatus.AFGEHANDELD)) {
            return "order/delete_order_cannotdelete";
        }
        else {
            return "order/delete_order";
        }
    }
    
    @GetMapping(value="/deleteconfirm/{id}")
    public String deleteOrderExecution(@PathVariable Long id, Model model) {
        Order order = orderRepository.findOne(id);
        updateProductStockAfterDeletingOrder(order.getOrderItemList());
        orderRepository.delete(order);
        
        
        return "redirect:/orders";
    }
    
    // Utility methods for managing lists and updating stock
    
    protected BigDecimal calculateOrderPrice(List<OrderItem> orderItemList) {
        BigDecimal price = new BigDecimal("0.00");
        for(OrderItem orderItem: orderItemList){
            price = price.add(orderItem.getSubTotal());

        }
        return price;
    }
    
    protected void cleanProductList(List<Product> productList, List<OrderItem> orderItemList) {
        for(OrderItem orderItem: orderItemList) {
            Product product = orderItem.getProduct();
            if(productList.contains(product))
                productList.remove(product);
        }
    }
    
    protected void updateProductStockAfterCreatingOrder(List<OrderItem> orderItemList) {
        
        for(OrderItem orderItem: orderItemList) {
                Product product = orderItem.getProduct();
                
                int amount = orderItem.getAmount();
                int oldStock = product.getStock();
                product.setStock(oldStock - amount);
                
                productRepository.save(product);
        }
    }

    protected void updateProductStockAfterDeletingOrder(List<OrderItem> orderItemList) {
        
        for(OrderItem orderItem: orderItemList) {
            Product product = orderItem.getProduct();
            
            int amount = orderItem.getAmount();
            int oldStock = product.getStock();
            product.setStock(oldStock + amount);
            
            productRepository.save(product);
        }
    }
    
     public OrderItem findOrderItem(Long productId) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(productRepository.findOne(productId));
        Example<OrderItem> example = Example.of(orderItem);
        return orderItemRepository.findOne(example);        
    }
}
