/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *
 * @author hwkei
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private CustomAuthenticationProvider authProvider;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    .exceptionHandling()
                .accessDeniedPage("/access_denied");
        http
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/home")
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .authorizeRequests().antMatchers("/static/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/accounts/**").hasRole("ADMIN")
                .and()
                .authorizeRequests().anyRequest().authenticated();

    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
        // Tijdelijk extra account om in te loggen als database nog niet gevuld is
        auth.inMemoryAuthentication().withUser("admin").password("welkom").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("medewerker").password("welkom").roles("MEDEWERKER");
        auth.inMemoryAuthentication().withUser("klant").password("welkom").roles("KLANT");
    }

}
