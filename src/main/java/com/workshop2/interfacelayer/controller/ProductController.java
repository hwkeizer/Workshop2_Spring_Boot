/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2.interfacelayer.controller;

import com.workshop2.domain.Product;
import com.workshop2.interfacelayer.repository.ProductRepository;
import java.math.BigDecimal;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Ahmed-Al-Alaaq(Egelantier)
 */
@Controller
@RequestMapping(path = "/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(path = "/add")
    public String addProduct(Model model) {
        model.addAttribute(new Product());
        return "addNewProduct";
    }

    @GetMapping
    public String Products(Model model) {

        model.addAttribute("productList", productRepository.findAll());
        return "products";
    }

    @PostMapping(path = "/add")
    public String addNewProduct(@Valid Product product, RedirectAttributes model, Errors errors) {
        
        if (errors.hasErrors()) {
            model.addAttribute(new Product()); 
            return "addNewProduct";
        }

        productRepository.save(product);
         model.addAttribute("productList", productRepository.findAll());
        return ("redirect:/products");
    }

  /*  @GetMapping(path = "/add")
    public @ResponseBody
    String addNewProduct(
            @RequestParam String name,
            @RequestParam BigDecimal price,
            @RequestParam int stock) {

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);

        productRepository.save(product);
        return "Saved";
    }

   @GetMapping(path="/all")
    public @ResponseBody Iterable<Product> getAllProducts() {
     return productRepository.findAll();
    }
     /*
    @GetMapping
    public String products(Model model) {

        // Tijdelijke code om producttabel even te vullen:
        addNewProduct("Goudse belegen kaas", new BigDecimal("12.99"), 134);
        addNewProduct("Leidse oude kaas", new BigDecimal("14.65"), 89);
        addNewProduct("Schimmelkaas", new BigDecimal("11.74"), 256);

        model.addAttribute("productList", productRepository.findAll());
        return "products";
    }*/

}
