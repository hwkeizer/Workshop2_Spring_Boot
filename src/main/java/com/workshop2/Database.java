/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop2;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.workshop2.domain.Account;
import static com.workshop2.domain.AccountType.*;
import com.workshop2.domain.Address;
import static com.workshop2.domain.Address.AddressType.*;
import com.workshop2.domain.Customer;
import com.workshop2.domain.Order;
import com.workshop2.domain.OrderItem;
import static com.workshop2.domain.OrderStatus.*;
import com.workshop2.domain.Product;
import com.workshop2.PasswordHash;
import com.workshop2.domain.Address.AddressType;
import com.workshop2.interfacelayer.controller.AccountController;
import com.workshop2.interfacelayer.repository.AccountRepository;
import com.workshop2.interfacelayer.repository.AddressRepository;
import com.workshop2.interfacelayer.repository.CustomerRepository;
import com.workshop2.interfacelayer.repository.OrderItemRepository;
import com.workshop2.interfacelayer.repository.OrderRepository;
import com.workshop2.interfacelayer.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author thoma
 */
@Component
public class Database {
    private static final Logger log = LoggerFactory.getLogger(Database.class);

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductRepository productRepository;
        
    public void initializeDatabase() {

        // clear all current data
        addressRepository.deleteAll();
        System.out.println("ADDRESS deleted");
        orderRepository.deleteAll();   
        System.out.println("ORDER deleted");
        customerRepository.deleteAll();  
        System.out.println("CUSTOMER deleted");
        accountRepository.deleteAll();
        System.out.println("ACCOUNT deleted");
        orderItemRepository.deleteAll();   
        System.out.println("ORDERITEM deleted");
        productRepository.deleteAll();   
        System.out.println("PRODUCT deleted");

        // Account
        String pass1 = PasswordHash.generateHash("welkom");
        String pass2 = PasswordHash.generateHash("welkom");
        String pass3 = PasswordHash.generateHash("welkom");
        Account account1 = new Account();
        account1.setUsername("piet");
        account1.setPassword(pass1);
        account1.setAccountType(ADMIN);
        Account account2 = new Account();
        account2.setUsername("klaas");
        account2.setPassword(pass2);
        account2.setAccountType(MEDEWERKER);
        Account account3 = new Account();
        account3.setUsername("jan");
        account3.setPassword(pass3);
        account3.setAccountType(KLANT);
        Account account4 = new Account();
        account4.setUsername("fred");
        account4.setPassword(pass1);
        account4.setAccountType(KLANT);
        Account account5 = new Account();
        account5.setUsername("joost");
        account5.setPassword(pass2);
        account5.setAccountType(KLANT);
        Account account6 = new Account();
        account6.setUsername("jaap");
        account6.setPassword(pass3);
        account6.setAccountType(KLANT);

        // Customer
        Customer customer1 = new Customer();
        customer1.setFirstName("Piet");
        customer1.setLastName("Pietersen");
        customer1.setLastNamePrefix(null);
        customer1.setAccount(account1);
        Customer customer2 = new Customer();
        customer2.setFirstName("Klaas");
        customer2.setLastName("Klaassen");
        customer2.setLastNamePrefix("van");
        customer2.setAccount(account2);
        Customer customer3 = new Customer();
        customer3.setFirstName("Jan");
        customer3.setLastName("Jansen");
        customer3.setLastNamePrefix(null);
        customer3.setAccount(account3);
        Customer customer4 = new Customer();
        customer4.setFirstName("Fred");
        customer4.setLastName("Horst");
        customer4.setLastNamePrefix("ter");
        customer4.setAccount(account4);
        Customer customer5 = new Customer();
        customer5.setFirstName("Joost");
        customer5.setLastName("Draaier");
        customer5.setLastNamePrefix("den");
        customer5.setAccount(account5);
        
        // Address
        Address address1 = new Address();
        address1.setStreetName("Postweg");
        address1.setNumber(201);
        address1.setAddition("h");
        address1.setPostalCode("3781JK");
        address1.setCity("Aalst");
        address1.setCustomer(customer1);
        address1.setAddressType(POSTADRES);
        Address address2 = new Address();
        address2.setStreetName("Snelweg");
        address2.setNumber(56);
        address2.setAddition(null);
        address2.setPostalCode("3922JL");
        address2.setCity("Ee");
        address2.setCustomer(customer2);
        address2.setAddressType(POSTADRES);
        Address address3 = new Address();
        address3.setStreetName("Torenstraat");
        address3.setNumber(82);
        address3.setAddition(null);
        address3.setPostalCode("7620CX");
        address3.setCity("Best");
        address3.setCustomer(customer2);
        address3.setAddressType(FACTUURADRES);
        Address address4 = new Address();
        address4.setStreetName("Valkstraat");
        address4.setNumber(9);
        address4.setAddition("e");
        address4.setPostalCode("2424DF");
        address4.setCity("Goorle");
        address4.setCustomer(customer2);
        address4.setAddressType(BEZORGADRES);
        Address address5 = new Address();
        address5.setStreetName("Dorpsstraat");
        address5.setNumber(5);
        address5.setAddition(null);
        address5.setPostalCode("9090NM");
        address5.setCity("Best");
        address5.setCustomer(customer3);
        address5.setAddressType(POSTADRES);
        Address address6 = new Address();
        address6.setStreetName("Plein");
        address6.setNumber(45);
        address6.setAddition(null);
        address6.setPostalCode("2522BH");
        address6.setCity("Oss");
        address6.setCustomer(customer4);
        address6.setAddressType(POSTADRES);
        Address address7 = new Address();
        address7.setStreetName("Maduralaan");
        address7.setNumber(23);
        address7.setAddition(null);
        address7.setPostalCode("8967HJ");
        address7.setCity("Apeldoorn");
        address7.setCustomer(customer5);
        address7.setAddressType(POSTADRES);
        
        
        // Order
        Order order1 = new Order();
        order1.setTotalPrice(new BigDecimal("230.78"));
        order1.setDateTime(LocalDateTime.now());
        order1.setOrderStatus(AFGEHANDELD);
        //customer1.addToOrderList(order1);
        order1.setCustomer(customer1);
        Order order2 = new Order();
        order2.setTotalPrice(new BigDecimal("62.97"));
        order2.setDateTime(LocalDateTime.now());
        order2.setOrderStatus(AFGEHANDELD);
        //customer1.addToOrderList(order2);
        order2.setCustomer(customer1);
        Order order3 = new Order();
        order3.setTotalPrice(new BigDecimal("144.12"));
        order3.setDateTime(LocalDateTime.now());
        order3.setOrderStatus(IN_BEHANDELING);
        //customer1.addToOrderList(order3);
        order3.setCustomer(customer1);
        Order order4 = new Order();
        order4.setTotalPrice(new BigDecimal("78.23"));
        order4.setDateTime(LocalDateTime.now());
        order4.setOrderStatus(AFGEHANDELD);
        //customer2.addToOrderList(order4);
        order4.setCustomer(customer2);
        Order order5 = new Order();
        order5.setTotalPrice(new BigDecimal("6.45"));
        order5.setDateTime(LocalDateTime.now());
        order5.setOrderStatus(NIEUW);
        //customer3.addToOrderList(order5);
        order5.setCustomer(customer3);
        Order order6 = new Order();
        order6.setTotalPrice(new BigDecimal("324.65"));
        order6.setDateTime(LocalDateTime.now());
        order6.setOrderStatus(AFGEHANDELD);
        //customer3.addToOrderList(order6);
        order6.setCustomer(customer3);
        Order order7 = new Order();
        order7.setTotalPrice(new BigDecimal("46.08"));
        order7.setDateTime(LocalDateTime.now());
        order7.setOrderStatus(IN_BEHANDELING);
        //customer3.addToOrderList(order7);
        order7.setCustomer(customer3);
        Order order8 = new Order();
        order8.setTotalPrice(new BigDecimal("99.56"));
        order8.setDateTime(LocalDateTime.now());
        order8.setOrderStatus(NIEUW);
        //customer4.addToOrderList(order8);
        order8.setCustomer(customer4);
        Order order9 = new Order();
        order9.setTotalPrice(new BigDecimal("23.23"));
        order9.setDateTime(LocalDateTime.now());
        order9.setOrderStatus(AFGEHANDELD);
        //customer5.addToOrderList(order9);
        order9.setCustomer(customer5);
        

        // Product
        Product product1 = new Product();
        product1.setName("Goudse belegen kaas");
        product1.setPrice(new BigDecimal("12.99"));
        product1.setStock(134);
        Product product2 = new Product();
        product2.setName("Goudse extra belegen kaas");
        product2.setPrice(new BigDecimal("14.70"));
        product2.setStock(239);
        Product product3 = new Product();
        product3.setName("Leidse oude kaas");
        product3.setPrice(new BigDecimal("14.65"));
        product3.setStock(89);
        Product product4 = new Product();
        product4.setName("Schimmelkaas");
        product4.setPrice(new BigDecimal("11.74"));
        product4.setStock(256);
        Product product5 = new Product();
        product5.setName("Leidse jonge kaas");
        product5.setPrice(new BigDecimal("11.24"));
        product5.setStock(122);
        Product product6 = new Product();
        product6.setName("Boeren jonge kaas");
        product6.setPrice(new BigDecimal("12.57"));
        product6.setStock(85);
        
        
        
        
        // OrderItem
        OrderItem orderItem1 = new OrderItem();
        order1.addToOrderItemList(orderItem1);
        orderItem1.setProduct(product6);
        orderItem1.setAmount(23);
        orderItem1.setSubTotal(new BigDecimal("254.12"));
        OrderItem orderItem2 = new OrderItem();
        order1.addToOrderItemList(orderItem2);
        orderItem2.setProduct(product1);
        orderItem2.setAmount(26);
        orderItem2.setSubTotal(new BigDecimal("345.20"));
        OrderItem orderItem3 = new OrderItem();
        order1.addToOrderItemList(orderItem3);
        orderItem3.setProduct(product2);
        orderItem3.setAmount(2);
        orderItem3.setSubTotal(new BigDecimal("24.14"));
        OrderItem orderItem4 = new OrderItem();
        order2.addToOrderItemList(orderItem4);
        orderItem4.setProduct(product1);
        orderItem4.setAmount(25);
        orderItem4.setSubTotal(new BigDecimal("289.89"));
        OrderItem orderItem5 = new OrderItem();
        order3.addToOrderItemList(orderItem5);
        orderItem5.setProduct(product4);
        orderItem5.setAmount(2);
        orderItem5.setSubTotal(new BigDecimal("34.89"));
        OrderItem orderItem6 = new OrderItem();
        order4.addToOrderItemList(orderItem6);
        orderItem6.setProduct(product2);
        orderItem6.setAmount(13);
        orderItem6.setSubTotal(new BigDecimal("156.76"));
        OrderItem orderItem7 = new OrderItem();
        order4.addToOrderItemList(orderItem7);
        orderItem7.setProduct(product5);
        orderItem7.setAmount(2);
        orderItem7.setSubTotal(new BigDecimal("23.78"));
        OrderItem orderItem8 = new OrderItem();
        order5.addToOrderItemList(orderItem8);
        orderItem8.setProduct(product2);
        orderItem8.setAmount(2);
        orderItem8.setSubTotal(new BigDecimal("21.34"));
        OrderItem orderItem9 = new OrderItem();
        order6.addToOrderItemList(orderItem9);
        orderItem9.setProduct(product1);
        orderItem9.setAmount(3);
        orderItem9.setSubTotal(new BigDecimal("35.31"));
        OrderItem orderItem10 = new OrderItem();
        order6.addToOrderItemList(orderItem10);
        orderItem10.setProduct(product3);
        orderItem10.setAmount(1);
        orderItem10.setSubTotal(new BigDecimal("11.23"));
        OrderItem orderItem11 = new OrderItem();
        order7.addToOrderItemList(orderItem11);
        orderItem11.setProduct(product6);
        orderItem11.setAmount(1);
        orderItem11.setSubTotal(new BigDecimal("14.23"));
        OrderItem orderItem12 = new OrderItem();
        order7.addToOrderItemList(orderItem12);
        orderItem12.setProduct(product2);
        orderItem12.setAmount(3);
        orderItem12.setSubTotal(new BigDecimal("31.87"));
        OrderItem orderItem13 = new OrderItem();
        order8.addToOrderItemList(orderItem13);
        orderItem13.setProduct(product4);
        orderItem13.setAmount(23);
        orderItem13.setSubTotal(new BigDecimal("167.32"));
        OrderItem orderItem14 = new OrderItem();
        order9.addToOrderItemList(orderItem14);
        orderItem14.setProduct(product1);
        orderItem14.setAmount(1);
        orderItem14.setSubTotal(new BigDecimal("11.34"));
        OrderItem orderItem15 = new OrderItem();
        order9.addToOrderItemList(orderItem15);
        orderItem15.setProduct(product2);
        orderItem15.setAmount(2);
        orderItem15.setSubTotal(new BigDecimal("22.41"));
        
        
                
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        productRepository.save(product4);
        productRepository.save(product5);
        productRepository.save(product6);
        System.out.println("PRODUCTS SAVED");
        orderItemRepository.save(orderItem1);
        orderItemRepository.save(orderItem2);
        orderItemRepository.save(orderItem3);
        orderItemRepository.save(orderItem4);
        orderItemRepository.save(orderItem5);
        orderItemRepository.save(orderItem6);
        orderItemRepository.save(orderItem7);
        orderItemRepository.save(orderItem8);
        orderItemRepository.save(orderItem9);
        orderItemRepository.save(orderItem10);
        orderItemRepository.save(orderItem11);
        orderItemRepository.save(orderItem12);
        orderItemRepository.save(orderItem13);
        orderItemRepository.save(orderItem14);
        orderItemRepository.save(orderItem15);
        System.out.println("ORDERITEMS SAVED");
        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
        customerRepository.save(customer4);
        customerRepository.save(customer5);
        System.out.println("CUSTOMERS SAVED");
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        orderRepository.save(order4);
        orderRepository.save(order5);
        orderRepository.save(order6);
        orderRepository.save(order7);
        orderRepository.save(order8);
        orderRepository.save(order9);
        System.out.println("ORDERS SAVED");
        accountRepository.save(account1);
        accountRepository.save(account2);
        accountRepository.save(account3);
        accountRepository.save(account4);
        accountRepository.save(account5);
        accountRepository.save(account6);
        System.out.println("ACCOUNTS SAVED");
        addressRepository.save(address1);
        addressRepository.save(address2);
        addressRepository.save(address3);
        addressRepository.save(address4);
        addressRepository.save(address5);
        addressRepository.save(address6);
        addressRepository.save(address7);
        System.out.println("ADDRESSES SAVED");

    }
//        // Prepare the SQL statements to create the DATABASE and recreate it
//        String createDatabase = "CREATE DATABASE IF NOT EXISTS " + databaseName;
//        String create_account_type = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`account_type` (`id` INT NOT NULL AUTO_INCREMENT, `type` VARCHAR(45) NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB";
//        String create_account = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`account` (`id` INT NOT NULL AUTO_INCREMENT, `username` VARCHAR(25) NOT NULL, `password` VARCHAR(180) NOT NULL, `account_type_id` INT NOT NULL, PRIMARY KEY (`id`), INDEX `fk_account_account_type1_idx` (`account_type_id` ASC), UNIQUE INDEX `username_UNIQUE` (`username` ASC), CONSTRAINT `fk_account_account_type1` FOREIGN KEY (`account_type_id`) REFERENCES `"+databaseName+"`.`account_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB";
//        String create_customer = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`customer` (`id` INT NOT NULL AUTO_INCREMENT, `first_name` VARCHAR(50) NOT NULL, `last_name` VARCHAR(50) NOT NULL, `ln_prefix` VARCHAR(15) NULL, `account_id` INT NULL, PRIMARY KEY (`id`), INDEX `fk_klant_account1_idx` (`account_id` ASC), CONSTRAINT `fk_klant_account1` FOREIGN KEY (`account_id`) REFERENCES `"+databaseName+"`.`account` (`id`) ON DELETE SET NULL ON UPDATE NO ACTION) ENGINE = InnoDB";
//        String address_type = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`address_type` (`id` INT NOT NULL AUTO_INCREMENT, `type` VARCHAR(45) NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB";
//        String address = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`address` (`id` INT NOT NULL AUTO_INCREMENT, `street_name` VARCHAR(50) NOT NULL, `number` INT NOT NULL, `addition` VARCHAR(5) NULL, `postal_code` VARCHAR(6) NOT NULL, `city` VARCHAR(45) NOT NULL, `customer_id` INT NOT NULL, `address_type_id` INT NOT NULL, PRIMARY KEY (`id`), INDEX `fk_adres_klant_idx` (`customer_id` ASC), INDEX `fk_adres_adres_type1_idx` (`address_type_id` ASC), CONSTRAINT `fk_adres_klant` FOREIGN KEY (`customer_id`) REFERENCES `"+databaseName+"`.`customer` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION, CONSTRAINT `fk_adres_adres_type1` FOREIGN KEY (`address_type_id`) REFERENCES `"+databaseName+"`.`address_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB";
//        String order_status = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`order_status` (`id` INT NOT NULL AUTO_INCREMENT, `status` VARCHAR(45) NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB";
//        String order = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`order` (`id` INT NOT NULL AUTO_INCREMENT, `total_price` DECIMAL(6,2) NOT NULL, `customer_id` INT NOT NULL, `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, `order_status_id` INT NOT NULL, PRIMARY KEY (`id`), INDEX `fk_bestelling_klant1_idx` (`customer_id` ASC),  INDEX `fk_bestelling_bestelling_status1_idx` (`order_status_id` ASC), CONSTRAINT `fk_bestelling_klant1` FOREIGN KEY (`customer_id`) REFERENCES `"+databaseName+"`.`customer` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION, CONSTRAINT `fk_bestelling_bestelling_status1` FOREIGN KEY (`order_status_id`) REFERENCES `"+databaseName+"`.`order_status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB";
//        String product = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`product` (`id` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(45) NOT NULL, `price` DECIMAL(6,2) NOT NULL, `stock` INT NOT NULL, PRIMARY KEY (`id`), UNIQUE INDEX `name_UNIQUE` (`name` ASC)) ENGINE = InnoDB";
//        String order_item = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`order_item` (`id` INT NOT NULL AUTO_INCREMENT, `order_id` INT NOT NULL, `product_id` INT NULL, `amount` INT NOT NULL, `subtotal` DECIMAL(6,2) NOT NULL, INDEX `fk_bestel_regel_bestelling1_idx` (`order_id` ASC), INDEX `fk_bestel_regel_artikel1_idx` (`product_id` ASC), PRIMARY KEY (`id`), CONSTRAINT `fk_bestel_regel_bestelling1` FOREIGN KEY (`order_id`) REFERENCES `"+databaseName+"`.`order` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION, CONSTRAINT `fk_bestel_regel_artikel1` FOREIGN KEY (`product_id`) REFERENCES `"+databaseName+"`.`product` (`id`) ON DELETE SET NULL ON UPDATE NO ACTION) ENGINE = InnoDB";
//        String trigger = "CREATE DEFINER = CURRENT_USER TRIGGER `"+databaseName+"`.`customer_BEFORE_DELETE` BEFORE DELETE ON `customer` FOR EACH ROW DELETE FROM account WHERE id = OLD.account_id;";
//        
//        try (Connection connection = getConnection();) {
//            Statement stat = connection.createStatement();
//            stat.executeUpdate(createDatabase); // Executes the given SQL statement, which may be an INSERT, UPDATE, or DELETE statement or an SQL statement that returns nothing, such as an SQL DDL statement. ExecuteQuery kan niet gebruikt worden voor DDL statements
//            stat.executeUpdate(create_account_type);
//            stat.executeUpdate(create_account);
//            stat.executeUpdate(create_customer);
//            stat.executeUpdate(address_type);
//            stat.executeUpdate(address);            
//            stat.executeUpdate(order_status);
//            stat.executeUpdate(order);
//            stat.executeUpdate(product);
//            stat.executeUpdate(order_item);            
//            stat.executeUpdate(trigger);
//        } catch  (SQLException ex) {
//            System.out.println("SQLException" + ex);
//        }
//        
//        log.debug("Database initialized");
//    }
//    
//    void populateDatabase()  {
//
//        // Generate the required password hashes
//            String pass1 = PasswordHash.generateHash("welkom");
//            String pass2 = PasswordHash.generateHash("welkom");
//            String pass3 = PasswordHash.generateHash("welkom");
//            String pass4 = PasswordHash.generateHash("geheim");
//            String pass5 = PasswordHash.generateHash("welkom");
//            String pass6 = PasswordHash.generateHash("welkom");
//        // Prepare the SQL statements to insert the test data into the DATABASE
//        String insert_account_type = "INSERT INTO `"+databaseName+"`.`account_type`(`id`,`type`) VALUES (1,\"admin\"),(2,\"medewerker\"),(3,\"klant\")";
//        String insert_address_type = "INSERT INTO `"+databaseName+"`.`address_type`(`id`,`type`) VALUES (1,\"postadres\"),(2,\"factuuradres\"),(3,\"bezorgadres\")";
//        String insert_order_status = "INSERT INTO `"+databaseName+"`.`order_status`(`id`,`status`) VALUES (1,\"nieuw\"),(2,\"in behandeling\"),(3,\"afgehandeld\")";
//        String insert_account = "INSERT INTO `"+databaseName+"`.`account`(`id`,`username`,`password`,`account_type_id`) VALUES (1,\"piet\",\""+pass1+"\",1),(2,\"klaas\",\""+pass2+"\",2),(3,\"jan\",\""+pass3+"\",3),(4,\"fred\",\""+pass4+"\",3),(5,\"joost\",\""+pass5+ "\",3),(6,\"jaap\",\""+pass6+"\",3)";
//        String insert_customer = "INSERT INTO `"+databaseName+"`.`customer`(`id`,`first_name`,`last_name`,`ln_prefix`,`account_id`) VALUES (1,\"Piet\",\"Pietersen\",null,1), (2,\"Klaas\",\"Klaassen\",null,2),(3,\"Jan\",\"Jansen\",null,3),(4,\"Fred\",\"Boomsma\",null,4),(5,\"Joost\",\"Snel\",null,5)";
//        String insert_address = "INSERT INTO `"+databaseName+"`.`address`(`id`,`street_name`,`number`,`addition`,`postal_code`,`city`,`customer_id`,`address_type_id`) VALUES (1,\"Postweg\",201,\"h\",\"3781JK\",\"Aalst\",1,1),(2,\"Snelweg\",56,null,\"3922JL\",\"Ee\",2,1),(3,\"Torenstraat\",82,null,\"7620CX\",\"Best\",2,2),(4,\"Valkstraat\",9,\"e\",\"2424DF\",\"Goorle\",2,3),(5,\"Dorpsstraat\",5,null,\"9090NM\",\"Best\",3,1),(6,\"Plein\",45,null,\"2522BH\",\"Oss\",4,1),(7,\"Maduralaan\",23,null,\"8967HJ\",\"Apeldoorn\",5,1)";
//        String insert_order = "INSERT INTO `"+databaseName+"`.`order`(`id`,`total_price`,`customer_id`,`date`,`order_status_id`) VALUES (1,230.78,1,\"2016-01-01 01:01:01\",3),(2,62.97,1,\"2016-05-02 01:01:01\",3),(3,144.12,1,\"2017-03-02 01:01:01\",2),(4,78.23,2,\"2017-04-08 01:01:01\",3),(5,6.45,3,\"2017-06-28 01:01:01\",1),(6,324.65,3,\"2017-06-07 01:01:01\",3),(7,46.08,3,\"2017-07-07 01:01:01\",2),(8,99.56,4,\"2017-07-17 01:01:01\",1),(9,23.23,5,\"2017-07-13 01:01:01\",3)";
//        String insert_product = "INSERT INTO `"+databaseName+"`.`product`(`id`,`name`,`price`,`stock`) VALUES (1,\"Goudse belegen kaas\",12.90,134),(2,\"Goudse extra belegen kaas\",14.70,239),(3,\"Leidse oude kaas\",14.65,89),(4,\"Schimmelkaas\",11.74,256),(5,\"Leidse jonge kaas\",11.24,122),(6,\"Boeren jonge kaas\",12.57,85)";
//        String insert_order_item = "INSERT INTO `"+databaseName+"`.`order_item`(`id`,`order_id`,`product_id`,`amount`,`subtotal`) VALUES (1,1,6,23,254.12),(2,1,1,26,345.20),(3,1,2,2,24.14),(4,2,1,25,289.89),(5,3,4,2,34.89),(6,4,2,13,156.76),(7,4,5,2,23.78),(8,5,2,2,21.34),(9,6,1,3,35.31),(10,6,3,1,11.23),(11,7,6,1,14.23),(12,7,2,3,31.87),(13,8,4,23,167.32),(14,9,1,1,11.34),(15,9,2,2,22.41)"; 
//        try (Connection connection = getConnection();) {
//            // Execute the SQL statements to insert the test data into the DATABASE
//            Statement stat = connection.createStatement();
//            stat.executeUpdate(insert_account_type);
//            stat.executeUpdate(insert_address_type);
//            stat.executeUpdate(insert_order_status);
//            stat.executeUpdate(insert_account);
//            stat.executeUpdate(insert_customer);
//            stat.executeUpdate(insert_address);
//            stat.executeUpdate(insert_order);
//            stat.executeUpdate(insert_product);
//            stat.executeUpdate(insert_order_item);            
//        } catch (SQLException ex) {
//            System.out.println("SQLException" + ex);
//        }
//        
//        log.debug("Database populated with testdata");
//    }
    
}
