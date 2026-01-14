package com.demo.stockchecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Stock Checker application.
 * This application provides REST APIs for checking and managing stock prices.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
@SpringBootApplication
public class StockCheckerApplication {

    /**
     * Main entry point for the Spring Boot application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(StockCheckerApplication.class, args);
    }
}
