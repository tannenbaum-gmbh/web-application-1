package com.demo.stockchecker.repository;

import com.demo.stockchecker.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository interface for managing stock data.
 * Uses Spring Data JPA with H2 database.
 * 
 * @author Demo Team
 * @version 2.0.0
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, String> {

    /**
     * Finds a stock by its symbol (case-insensitive).
     * 
     * @param symbol the stock symbol
     * @return an Optional containing the stock if found
     */
    Optional<Stock> findBySymbolIgnoreCase(String symbol);

    /**
     * Checks if a stock exists by symbol (case-insensitive).
     * 
     * @param symbol the stock symbol
     * @return true if exists
     */
    boolean existsBySymbolIgnoreCase(String symbol);

    /**
     * Deletes a stock by symbol.
     * 
     * @param symbol the stock symbol
     */
    void deleteBySymbol(String symbol);
}
