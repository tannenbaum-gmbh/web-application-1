package com.demo.stockchecker.exception;

/**
 * Exception thrown when a requested stock is not found in the repository.
 * This is a runtime exception that will be handled by the global exception handler.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
public class StockNotFoundException extends RuntimeException {

    private final String symbol;

    /**
     * Constructor with stock symbol.
     * 
     * @param symbol the stock symbol that was not found
     */
    public StockNotFoundException(String symbol) {
        super(String.format("Stock with symbol '%s' not found", symbol));
        this.symbol = symbol;
    }

    /**
     * Constructor with custom message and stock symbol.
     * 
     * @param message the custom error message
     * @param symbol the stock symbol that was not found
     */
    public StockNotFoundException(String message, String symbol) {
        super(message);
        this.symbol = symbol;
    }

    /**
     * Gets the stock symbol that was not found.
     * 
     * @return the stock symbol
     */
    public String getSymbol() {
        return symbol;
    }
}
