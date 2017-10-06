/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author hwkei
 */
@Entity
@Table(name = "ACCOUNT",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"USERNAME"})})
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @Column(name = "USERNAME")
    @NotNull(message="Account naam mag niet leeg zijn")
    @Size(min=3, max=16, message="Account naam moet minimaal 3 tot maximaal 16 tekens bevatten")
    private String username;
    @Column(name = "PASSWORD")
    @NotNull(message="Wachtwoord mag niet leeg zijn")
    @Size(min=5, max=250, message="Wachtwoord moet minimaal 5 tot maximaal 250 tekens bevatten")
    private String password;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "ACCOUNT_TYPE")
    private AccountType accountType;
    
    public Account() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    
    @Override
    public String toString(){
        return String.format("%-5d%-20s%-20s%-20s", this.getId(), this.getUsername(), "********", this.getAccountType());
    }
    
    public String toStringNoId(){
        return String.format("%-20s%-20s%-20s", this.getUsername(), "********", this.getAccountType());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.username);
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
        final Account other = (Account) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return true;
    }
    
    
}