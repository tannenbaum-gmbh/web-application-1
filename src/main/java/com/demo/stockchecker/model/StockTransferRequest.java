package com.demo.stockchecker.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a stock transfer request between countries.
 * This model contains information about stock symbol, quantity, and country details.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
public class StockTransferRequest {

    private String transferId;

    @NotBlank(message = "Stock symbol is required")
    private String stockSymbol;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Long quantity;

    @NotBlank(message = "From country is required")
    private String fromCountry;

    @NotBlank(message = "To country is required")
    private String toCountry;

    private String status;

    private LocalDateTime createdDate;

    private LocalDateTime lastUpdated;

    /**
     * Default constructor for StockTransferRequest.
     */
    public StockTransferRequest() {
        this.createdDate = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
        this.status = "PENDING";
    }

    /**
     * Parameterized constructor for StockTransferRequest.
     * 
     * @param transferId the unique transfer identifier
     * @param stockSymbol the stock symbol to transfer
     * @param quantity the quantity to transfer
     * @param fromCountry the source country
     * @param toCountry the destination country
     */
    public StockTransferRequest(String transferId, String stockSymbol, Long quantity, 
                                String fromCountry, String toCountry) {
        this.transferId = transferId;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.fromCountry = fromCountry;
        this.toCountry = toCountry;
        this.status = "PENDING";
        this.createdDate = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }

    // Getters and Setters

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getFromCountry() {
        return fromCountry;
    }

    public void setFromCountry(String fromCountry) {
        this.fromCountry = fromCountry;
    }

    public String getToCountry() {
        return toCountry;
    }

    public void setToCountry(String toCountry) {
        this.toCountry = toCountry;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.lastUpdated = LocalDateTime.now();
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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
        StockTransferRequest that = (StockTransferRequest) o;
        return Objects.equals(transferId, that.transferId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferId);
    }

    @Override
    public String toString() {
        return "StockTransferRequest{" +
                "transferId='" + transferId + '\'' +
                ", stockSymbol='" + stockSymbol + '\'' +
                ", quantity=" + quantity +
                ", fromCountry='" + fromCountry + '\'' +
                ", toCountry='" + toCountry + '\'' +
                ", status='" + status + '\'' +
                ", createdDate=" + createdDate +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
