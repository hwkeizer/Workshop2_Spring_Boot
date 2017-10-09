/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author hwkei
 */
@Entity
@Table(name = "ORDER_ITEM")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @OneToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
    @Column(name = "AMOUNT")
    @NotNull(message="Voer het aantal van dit product in dat u wilt bestellen")
    @Min(value=0, message="Aantal moet positief zijn, voer 0 in om product niet toe te voegen.")
    private Integer amount;
    @Column(name = "SUB_TOTAL")
    private BigDecimal subTotal;
    
    // Default no-arg constructor will leave all member fields on their default
    // except for the id field which will be invalidated to a negative value
    public OrderItem() {
        
    }
     // Constructor without id, id will be invalidated to a negative value
    public OrderItem(Product product, int amount, BigDecimal subTotal) {
        this.product = product;
        this.amount = amount;
        this.subTotal = subTotal;
    }
    
    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }
    
    @Override
    public String toString(){
        return "ID: " + this.id.toString() + "   amount: " + this.amount + "    subTotal: " + this.subTotal.toString();
    }

    public String toStringNoId(){
        return "   amount: " + this.amount + "    subTotal: " + this.subTotal.toString();
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + Objects.hashCode(this.product);
        hash = 83 * hash + Objects.hashCode(this.amount);
        hash = 83 * hash + Objects.hashCode(this.subTotal);
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
        final OrderItem other = (OrderItem) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.product, other.product)) {
            return false;
        }
        if (!Objects.equals(this.amount, other.amount)) {
            return false;
        }
        if (!Objects.equals(this.subTotal, other.subTotal)) {
            return false;
        }
        return true;
    }
    
}
