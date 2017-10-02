package com.workshop2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@EntityScan(
        basePackageClasses = {Applikaasie.class, Jsr310JpaConverters.class}
)
@SpringBootApplication
public class Applikaasie extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Applikaasie.class, args);
                                
	}
        
}
