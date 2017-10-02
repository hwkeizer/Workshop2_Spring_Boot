/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2.interfacelayer.controller;

import com.workshop2.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author thoma
 */
@Controller
@RequestMapping(path="/refreshdatabase")
public class DatabaseController {
    @Autowired
    private Database database;
    
    @GetMapping
    public String addNewCustomer() {
        
       database.initializeDatabase();
       return "databaserefreshed";
    }
}
