/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author thoma
 */
@Entity
@Table(name = "CUSTOMER")
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public Long id;
    @NotNull(message="Voornaam mag niet leeg zijn")
    @Size(min=1, max=16, message="Voornaam moet minimaal 1 tot maximaal 16 tekens bevatten")
    @Column(name = "FIRST_NAME")
    private String firstName;
    @NotNull(message="Achternaam mag niet leeg zijn")
    @Size(min=3, max=16, message="Achternaam moet minimaal 3 tot maximaal 16 tekens bevatten")
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "LAST_NAME_PREFIX")
    @Size(max=16, message="Tussenvoegsel mag maximaal 16 tekens bevatten")
    private String lastNamePrefix;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Order> orderList = new ArrayList<>();
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Address> addressList = new ArrayList<>();

    public Customer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastNamePrefix() {
        return lastNamePrefix;
    }

    public void setLastNamePrefix(String lastNamePrefix) {
        // prevent null in the output
        if (lastNamePrefix == null) {
            lastNamePrefix = "";
        }
        this.lastNamePrefix = lastNamePrefix;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void addToOrderList(Order order) {
        orderList.add(order);
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(Address address) {
        addressList.add(address);
    }

    @Override
    public String toString() {
        if (getLastNamePrefix() != null) {
            return String.format("%-5d%-20s%-15s%-20s", getId(), getFirstName(), getLastNamePrefix(), getLastName());
        } else {
            return String.format("%-5d%-20s%-15s%-20s", getId(), getFirstName(), "", getLastName());
        }
    }

    public String toStringNoId() {
        if (getLastNamePrefix() != null) {
            return String.format("%-20s%-15s%-20s", getFirstName(), getLastNamePrefix(), getLastName());
        } else {
            return String.format("%-20s%-15s%-20s", getFirstName(), "", getLastName());
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.firstName);
        hash = 89 * hash + Objects.hashCode(this.lastName);
        hash = 89 * hash + Objects.hashCode(this.lastNamePrefix);
        hash = 89 * hash + Objects.hashCode(this.account);
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
        final Customer other = (Customer) obj;
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.lastNamePrefix, other.lastNamePrefix)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.account, other.account)) {
            return false;
        }
        return true;
    }

}
