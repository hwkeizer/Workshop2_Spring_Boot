/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2.interfacelayer.controller;

import com.workshop2.domain.OrderItem;
import com.workshop2.domain.Product;
import com.workshop2.interfacelayer.repository.OrderItemRepository;
import com.workshop2.interfacelayer.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
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
 * @author Ahmed-Al-Alaaq(Egelantier)
 */
@Controller
@RequestMapping(path = "/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderController orderController;
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

     @RequestMapping(value="/delete/{name}", method = RequestMethod.GET)
    //@GetMapping(path = "/delete")
    public String ProductList(@PathVariable String name, Model model) {
        List naamList = getAllProductsNaam();
       
        model.addAttribute( getProduct(name));
        model.addAttribute("productNaamlist", naamList);
        return "deleteProduct";
    }

    @RequestMapping(value="/delete", method = RequestMethod.POST)
    public String deleteProduct(Product product, Model model, BindingResult result) {
          
        if (result.hasErrors()) {
            List naamList = getAllProductsNaam();
            model.addAttribute("productNaamlist", naamList);
            return "deleteProduct";
        }
       OrderItem orderItem = orderController.findOrderItem(product.getId());
       orderItem.setProduct(null);
        productRepository.delete(product);
        //model.addAttribute("productList", productRepository.findAll());
        return ("redirect:/products");
    }
       
    public List<String> getAllProductsNaam() {
        List<Product> products = productRepository.findAll();
        List<String> naamList = new ArrayList<>();
        for (Product product : products) {
            naamList.add(product.getName());
        }
        return naamList;
    }
    public Product getProduct(String productNaam){
         List<Product> products = productRepository.findAll();
         Product product = new Product();
         for (Product prod : products) 
            if(prod.getName().equals(productNaam))
                product = prod;
         return product;
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
