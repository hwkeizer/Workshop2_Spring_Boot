/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2.interfacelayer.repository;

import com.workshop2.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Ahmed-Al-Alaaq(Egelantier)
 */
public interface ProductRepository extends JpaRepository<Product, Long>{
   
}
