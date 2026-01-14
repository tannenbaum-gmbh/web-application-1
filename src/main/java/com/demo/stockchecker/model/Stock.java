package com.demo.stockchecker.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a stock with its current market information.
 * This model contains stock symbol, name, pricing information, and change metrics.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
public class Stock {

    @NotBlank(message = "Stock symbol is required")
    private String symbol;

    @NotBlank(message = "Stock name is required")
    private String name;

    @NotNull(message = "Current price is required")
    @Positive(message = "Current price must be positive")
    private BigDecimal currentPrice;

    @NotNull(message = "Price change is required")
    private BigDecimal change;

    @NotNull(message = "Change percent is required")
    private BigDecimal changePercent;

    @PositiveOrZero(message = "Volume must be zero or positive")
    private Long volume;

    private LocalDateTime lastUpdated;

    /**
     * Default constructor for Stock.
     */
    public Stock() {
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Parameterized constructor for Stock.
     * 
     * @param symbol the stock symbol (e.g., "AAPL", "GOOGL")
     * @param name the full name of the stock
     * @param currentPrice the current trading price
     * @param change the price change from previous close
     * @param changePercent the percentage change from previous close
     */
    public Stock(String symbol, String name, BigDecimal currentPrice, BigDecimal change, BigDecimal changePercent) {
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.change = change;
        this.changePercent = changePercent;
        this.volume = 0L;
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Full parameterized constructor for Stock.
     * 
     * @param symbol the stock symbol
     * @param name the full name of the stock
     * @param currentPrice the current trading price
     * @param change the price change
     * @param changePercent the percentage change
     * @param volume the trading volume
     */
    public Stock(String symbol, String name, BigDecimal currentPrice, BigDecimal change, 
                 BigDecimal changePercent, Long volume) {
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.change = change;
        this.changePercent = changePercent;
        this.volume = volume;
        this.lastUpdated = LocalDateTime.now();
    }

    // Getters and Setters

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
        this.lastUpdated = LocalDateTime.now();
    }

    public BigDecimal getChange() {
        return change;
    }

    public void setChange(BigDecimal change) {
        this.change = change;
    }

    public BigDecimal getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(BigDecimal changePercent) {
        this.changePercent = changePercent;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(symbol, stock.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", currentPrice=" + currentPrice +
                ", change=" + change +
                ", changePercent=" + changePercent +
                ", volume=" + volume +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
