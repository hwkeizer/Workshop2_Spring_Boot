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
    public class ScopedOrder extends Order {
        Long localId;
        private void setLocalId(Long id){
            this.localId = id;
        }
        
        private Long getLocalId() {
            return localId;
        }
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
        List<Customer> customerList = customerRepository.findAll();
        
        model.addAttribute("customerList", customerList);
        model.addAttribute("order", order);
        return "order/addOrder_selectcustomer";
    }
    
    @GetMapping(path="addorder_setcustomer/{id}")
    public String addOrderSetCustomer(@PathVariable Long id, Model model) {
        order.setCustomer(customerRepository.findOne(id));
        model.addAttribute("order", order);
        return "order/addOrder_start";
    }
    
    @GetMapping(path="addorder_noOrderItemSelected")
    public String returnToSelectOrderItem(Model model) {
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
                return("redirect:/orders/addorder_noOrderItemSelected");
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
    
    @GetMapping(value="edit/{id}")
    public String updateOrder(@PathVariable Long id, Model model) {
        Order order1 = orderRepository.findOne(id);
        order = new ScopedOrder();
        order.setLocalId(order1.getId());
        model.addAttribute(order1);
        if(order1.getOrderStatus().equals(OrderStatus.NIEUW)) {
            return "order/update_order_new";
        }
        else if(order1.getOrderStatus().equals(OrderStatus.IN_BEHANDELING)) {
            return "order/update_order_inprogress";
        }
        else {
            return "order/update_order_finished";
        }
    }
    
    @GetMapping(value="/edit_orderstatus_new")
    public String updateOrderStatusNew(Model model) {
        Order order1 = orderRepository.findOne(order.getLocalId());
        model.addAttribute("order", order1);
        return "order/update_orderstatus_new";
    }
    
    @GetMapping(value="/edit_orderstatus_inprogress")
    public String updateOrderStatusInProgress(Model model) {
        Order order1 = orderRepository.findOne(order.getLocalId());
        model.addAttribute("order", order1);
        return "order/update_orderstatus_inprogress";
    }
    
    @GetMapping(value="/update_orderstatus")
    public String updateOrderStatusExecution(Model model) {
        Order order1 = orderRepository.findOne(order.getLocalId());
        if(order1.getOrderStatus().equals(NIEUW)) {
            order1.setOrderStatus(IN_BEHANDELING);
        }
        else {
            order1.setOrderStatus(AFGEHANDELD);
        }
        orderRepository.save(order1);
        return "redirect:/orders/edit/"+ order.getLocalId();
    }
    
    @GetMapping(value="delete_orderitem/{id}")
    public String showDeleteOrderItem(@PathVariable Long id, Model model) {
        Order order1 = orderRepository.findOne(order.getLocalId());
        model.addAttribute("order", order1);
        if(order1.getOrderItemList().size() <= 1) {
            return "order/delete_orderitem_notallowed";
        }
        
        model.addAttribute(orderItemRepository.findOne(id));
        return "order/delete_orderitem";
    }
    
    @GetMapping(value="/delete_orderitem_confirm/{id}")
    public String deleteOrderItemExecution(@PathVariable Long id, Model model) {
                
        // delete the orderItem and update product stock
        OrderItem orderItem = orderItemRepository.findOne(id);
        updateProductStockAfterDeletingOrderItem(orderItem);
        orderItemRepository.delete(orderItem);
        
        // update order price
        Order order1 = orderRepository.findOne(order.getLocalId());
        order1.setTotalPrice(calculateOrderPrice(order1.getOrderItemList()));
        orderRepository.save(order1);
        
        System.out.println("RETURN TO ORDER PAGE REACHED");
        return "redirect:/orders/edit/"+ order.getLocalId();
    }
    
    @GetMapping(value="/add_orderitem_existing")
    public String addNewOrderItemToExistingOrder(Model model) {        
        List<Product> productList = productRepository.findAll();
        Order order1 = orderRepository.findOne(order.getLocalId());
        cleanProductList(productList, order1.getOrderItemList());
        
        // Only send products which have not yet been chosen
        if(productList.size() != 0) {
            model.addAttribute(new OrderItem());
            model.addAttribute("productList", productList);

            return "order/addNewOrderItem_existing";
        }
        else {
            return "order/addNewOrderItem_existing_noMoreProducts";
        }
    }
    
    @PostMapping(path="/addorderitem_existing")
    public String addNewOrderItemToExistingOrder(@Valid OrderItem orderItem, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            List<Product> productList = productRepository.findAll();
        
            // Only send products which have not yet been chosen
            cleanProductList(productList, order.getOrderItemList());
            model.addAttribute(new OrderItem());
            model.addAttribute("productList", productList);

            return "order/addNewOrderItem";
        }
        if(orderItem.getAmount() == 0) {
            return ("redirect:/orders/edit/" + order.getLocalId());
        }
        
        Order order1 = orderRepository.findOne(order.getLocalId());
        
        // get the product
        Long id = orderItem.getProduct().getId();
        Product product = productRepository.findOne(id);
        orderItem.setProduct(product);
        
        // calculate and set subtotal
        BigDecimal subTotal = product.getPrice().multiply(new BigDecimal(orderItem.getAmount()));
        orderItem.setSubTotal(subTotal);
        
        // add the orderItem to the Order
        order1.addToOrderItemList(orderItem);
        
        // update the product stock
        updateProductStockAfterAddingOrderItem(orderItem);
        
        // set totalPrice
        order1.setTotalPrice(calculateOrderPrice(order1.getOrderItemList()));
        
        // save order
        orderRepository.save(order1);

        return("redirect:/orders/edit/" + order.getLocalId());
    }
    
    @GetMapping(value="/return_to_edit_order")
    public String cannotAddMoreOrderItems() {
        return ("redirect:/orders/edit/" + order.getLocalId());
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
    

     public List<OrderItem> findOrderItems(Long productId) {
         List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItem item :orderItemRepository.findAll()){
        if (item.getProduct().getId().equals(productId))
        orderItems.add(item);
        }
        return orderItems;
     }
     
     public boolean isFound(Long productId){
         for (OrderItem item :orderItemRepository.findAll()){
              if (item.getProduct().getId().equals(productId))
                  return true;
         }
         return false;
     }
             
    

    protected void updateProductStockAfterDeletingOrderItem(OrderItem orderItem) {

        Product product = orderItem.getProduct();

        int amount = orderItem.getAmount();
        int oldStock = product.getStock();
        product.setStock(oldStock + amount);

        productRepository.save(product);
    }
    
    protected void updateProductStockAfterAddingOrderItem(OrderItem orderItem) {

        Product product = orderItem.getProduct();

        int amount = orderItem.getAmount();
        int oldStock = product.getStock();
        product.setStock(oldStock - amount);

        productRepository.save(product);
    }
    
     public OrderItem findOrderItem(Long productId) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(productRepository.findOne(productId));
        Example<OrderItem> example = Example.of(orderItem);
        return orderItemRepository.findOne(example);        
    }

}
