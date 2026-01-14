package com.demo.stockchecker.service;

import com.demo.stockchecker.model.Stock;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for stock operations.
 * Defines business logic methods for managing stocks.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
public interface StockService {

    /**
     * Retrieves all stocks.
     * 
     * @return a list of all stocks
     */
    List<Stock> getAllStocks();

    /**
     * Retrieves a stock by its symbol.
     * 
     * @param symbol the stock symbol
     * @return the stock
     * @throws com.demo.stockchecker.exception.StockNotFoundException if stock not found
     */
    Stock getStockBySymbol(String symbol);

    /**
     * Updates the price of a stock.
     * Automatically calculates change and change percent.
     * 
     * @param symbol the stock symbol
     * @param newPrice the new price
     * @return the updated stock
     * @throws com.demo.stockchecker.exception.StockNotFoundException if stock not found
     * @throws IllegalArgumentException if newPrice is null or not positive
     */
    Stock updateStockPrice(String symbol, BigDecimal newPrice);

    /**
     * Creates or updates a stock.
     * 
     * @param stock the stock to save
     * @return the saved stock
     * @throws IllegalArgumentException if stock data is invalid
     */
    Stock saveStock(Stock stock);

    /**
     * Deletes a stock by its symbol.
     * 
     * @param symbol the stock symbol
     * @return true if deleted, false if not found
     */
    boolean deleteStock(String symbol);

    /**
     * Checks if a stock exists.
     * 
     * @param symbol the stock symbol
     * @return true if exists, false otherwise
     */
    boolean stockExists(String symbol);
}
