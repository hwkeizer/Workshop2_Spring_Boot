/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2.config;

import com.workshop2.domain.AccountType;
import com.workshop2.interfacelayer.controller.AccountController;
import java.util.ArrayList;
import java.util.List;
import javax.naming.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 *
 * @author hwkei
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    AccountController accountController;
    
    @Override
    public Authentication authenticate(Authentication authentication) {
        
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        if (accountController.validateAccount(name, password)) {
            List<SimpleGrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority("ROLE_" + accountController.getUserRole(name).toString()));
            System.out.println("ROLES: " + roles);
            return new UsernamePasswordAuthenticationToken(name, password, roles);
        } else {
            return null;
        }
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
