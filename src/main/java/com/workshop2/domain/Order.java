/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/*
 *
 * @author hwkei
 */
@Entity
@Table(name = "`order`")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TOTAL_PRICE")
    private BigDecimal totalPrice;
    @Column(name = "DATE_TIME")
    private LocalDateTime dateTime;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "ORDER_STATUS")
    private OrderStatus orderStatus;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_ID")
    private List<OrderItem> orderItemList = new ArrayList<>();
    //@JoinColumn(name = "CUSTOMER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    // Default no-arg constructor will leave all member fields on their default
    // except for the id field which will be invalidated to a negative value
    public Order() {
        
    }
    
    // Constructor without id, id will be invalidated to a negative value
    public Order(BigDecimal totalPrice, LocalDateTime dateTime, OrderStatus orderStatus) {
        this.totalPrice = totalPrice;
        this.dateTime = dateTime;
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime date) {
        this.dateTime = date;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
    
    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }
    
    public void addToOrderItemList(OrderItem orderItem) {
        orderItemList.add(orderItem);
    }
    
    public void removeFromOrderItemList(OrderItem orderItem) {
        orderItemList.remove(orderItem);
    }
    
    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
        
    @Override
    public String toString() {
        return String.format("%-5d%-30s%-10.2f%-15s%-20s", this.getId(), this.getCustomer().getLastName(), this.getTotalPrice(), this.getDateTime().toLocalDate().toString(), this.getOrderStatus().toString());
    }

    public String toStringNoId() {
        return String.format("%-30s%-10.2f%-15s%-20s", this.getCustomer().getLastName(), this.getTotalPrice(), this.getDateTime().toLocalDate().toString(), this.getOrderStatus().toString());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.totalPrice);
//        hash = 53 * hash + Objects.hashCode(this.dateTime.getYear());
//        hash = 53 * hash + Objects.hashCode(this.dateTime.getMonthValue());
//        hash = 53 * hash + Objects.hashCode(this.dateTime.getDayOfMonth());
        hash = 53 * hash + Objects.hashCode(this.orderStatus);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.orderStatus, other.orderStatus)) {
            return false;
        }
        if (!Objects.equals(this.totalPrice, other.totalPrice)) {
            return false;
        }
        if (!Objects.equals(this.dateTime.getYear(), other.dateTime.getYear())) {
            return false;
        }
        if (!Objects.equals(this.dateTime.getMonthValue(), other.dateTime.getMonthValue())) {
            return false;
        }
        if (!Objects.equals(this.dateTime.getDayOfMonth(), other.dateTime.getDayOfMonth())) {
            return false;
        }
        return true;
    }
    
    public boolean equalsNoId(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (!Objects.equals(this.orderStatus, other.orderStatus)) {
            return false;
        }
        if (!Objects.equals(this.totalPrice, other.totalPrice)) {
            return false;
        }
        if (!Objects.equals(this.dateTime.getYear(), other.dateTime.getYear())) {
            return false;
        }
        if (!Objects.equals(this.dateTime.getMonthValue(), other.dateTime.getMonthValue())) {
            return false;
        }
        if (!Objects.equals(this.dateTime.getDayOfMonth(), other.dateTime.getDayOfMonth())) {
            return false;
        }
        return true;
    }
   
}
