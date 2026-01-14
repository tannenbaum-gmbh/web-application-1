package com.demo.stockchecker.repository;

import com.demo.stockchecker.model.Stock;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository class for managing stock data.
 * Uses in-memory storage (ConcurrentHashMap) for demo purposes.
 * Thread-safe implementation for concurrent access.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
@Repository
public class StockRepository {

    private final Map<String, Stock> stockStore = new ConcurrentHashMap<>();

    /**
     * Constructor that initializes the repository with sample stock data.
     */
    public StockRepository() {
        initializeSampleData();
    }

    /**
     * Initializes the repository with sample stock data for demo purposes.
     */
    private void initializeSampleData() {
        stockStore.put("AAPL", new Stock(
            "AAPL", 
            "Apple Inc.", 
            new BigDecimal("182.52"), 
            new BigDecimal("2.35"), 
            new BigDecimal("1.30"),
            125000000L
        ));
        
        stockStore.put("GOOGL", new Stock(
            "GOOGL", 
            "Alphabet Inc.", 
            new BigDecimal("140.93"), 
            new BigDecimal("-1.15"), 
            new BigDecimal("-0.81"),
            98000000L
        ));
        
        stockStore.put("MSFT", new Stock(
            "MSFT", 
            "Microsoft Corporation", 
            new BigDecimal("374.58"), 
            new BigDecimal("3.42"), 
            new BigDecimal("0.92"),
            87000000L
        ));
        
        stockStore.put("AMZN", new Stock(
            "AMZN", 
            "Amazon.com Inc.", 
            new BigDecimal("151.94"), 
            new BigDecimal("1.88"), 
            new BigDecimal("1.25"),
            102000000L
        ));
        
        stockStore.put("TSLA", new Stock(
            "TSLA", 
            "Tesla Inc.", 
            new BigDecimal("238.45"), 
            new BigDecimal("-5.23"), 
            new BigDecimal("-2.15"),
            145000000L
        ));
    }

    /**
     * Retrieves all stocks from the repository.
     * 
     * @return a list of all stocks
     */
    public List<Stock> findAll() {
        return new ArrayList<>(stockStore.values());
    }

    /**
     * Retrieves a stock by its symbol.
     * 
     * @param symbol the stock symbol (case-insensitive)
     * @return an Optional containing the stock if found, or empty if not found
     */
    public Optional<Stock> findBySymbol(String symbol) {
        return Optional.ofNullable(stockStore.get(symbol.toUpperCase()));
    }

    /**
     * Saves or updates a stock in the repository.
     * 
     * @param stock the stock to save or update
     * @return the saved stock
     */
    public Stock save(Stock stock) {
        stockStore.put(stock.getSymbol().toUpperCase(), stock);
        return stock;
    }

    /**
     * Deletes a stock from the repository by its symbol.
     * 
     * @param symbol the stock symbol to delete
     * @return true if the stock was deleted, false if not found
     */
    public boolean deleteBySymbol(String symbol) {
        return stockStore.remove(symbol.toUpperCase()) != null;
    }

    /**
     * Checks if a stock exists in the repository.
     * 
     * @param symbol the stock symbol to check
     * @return true if the stock exists, false otherwise
     */
    public boolean existsBySymbol(String symbol) {
        return stockStore.containsKey(symbol.toUpperCase());
    }

    /**
     * Returns the count of stocks in the repository.
     * 
     * @return the number of stocks
     */
    public long count() {
        return stockStore.size();
    }
}
