package com.demo.stockchecker.controller;

import com.demo.stockchecker.model.Stock;
import com.demo.stockchecker.service.StockService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for stock operations.
 * Provides endpoints for retrieving and updating stock information.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/stocks")
@Validated
public class StockController {

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);

    private final StockService stockService;

    /**
     * Constructor with dependency injection.
     * 
     * @param stockService the stock service
     */
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    /**
     * Retrieves all stocks.
     * 
     * @return ResponseEntity with list of all stocks and HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        logger.info("GET /api/stocks - Fetching all stocks");
        List<Stock> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }

    /**
     * Retrieves a specific stock by its symbol.
     * 
     * @param symbol the stock symbol
     * @return ResponseEntity with the stock and HTTP 200 status
     * @throws com.demo.stockchecker.exception.StockNotFoundException if stock not found (handled by GlobalExceptionHandler)
     */
    @GetMapping("/{symbol}")
    public ResponseEntity<Stock> getStockBySymbol(@PathVariable String symbol) {
        logger.info("GET /api/stocks/{} - Fetching stock", symbol);
        Stock stock = stockService.getStockBySymbol(symbol);
        return ResponseEntity.ok(stock);
    }

    /**
     * Updates the price of a specific stock.
     * 
     * @param symbol the stock symbol
     * @param priceUpdate map containing the new price
     * @return ResponseEntity with updated stock and HTTP 200 status
     * @throws com.demo.stockchecker.exception.StockNotFoundException if stock not found
     * @throws IllegalArgumentException if price is invalid
     */
    @PutMapping("/{symbol}/price")
    public ResponseEntity<Stock> updateStockPrice(
            @PathVariable String symbol,
            @RequestBody Map<String, BigDecimal> priceUpdate) {
        
        logger.info("PUT /api/stocks/{}/price - Updating stock price", symbol);
        
        BigDecimal newPrice = priceUpdate.get("price");
        if (newPrice == null) {
            throw new IllegalArgumentException("Price field is required in request body");
        }
        
        Stock updatedStock = stockService.updateStockPrice(symbol, newPrice);
        return ResponseEntity.ok(updatedStock);
    }

    /**
     * Creates or updates a stock.
     * 
     * @param stock the stock data
     * @return ResponseEntity with saved stock and HTTP 201 status if created, 200 if updated
     */
    @PostMapping
    public ResponseEntity<Stock> createStock(@Valid @RequestBody Stock stock) {
        logger.info("POST /api/stocks - Creating stock: {}", stock.getSymbol());
        
        boolean exists = stockService.stockExists(stock.getSymbol());
        Stock savedStock = stockService.saveStock(stock);
        
        if (exists) {
            return ResponseEntity.ok(savedStock);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStock);
        }
    }

    /**
     * Updates an existing stock.
     * 
     * @param symbol the stock symbol
     * @param stock the updated stock data
     * @return ResponseEntity with updated stock and HTTP 200 status
     * @throws com.demo.stockchecker.exception.StockNotFoundException if stock not found
     */
    @PutMapping("/{symbol}")
    public ResponseEntity<Stock> updateStock(
            @PathVariable String symbol,
            @Valid @RequestBody Stock stock) {
        
        logger.info("PUT /api/stocks/{} - Updating stock", symbol);
        
        // Verify stock exists
        stockService.getStockBySymbol(symbol);
        
        // Ensure symbol matches
        stock.setSymbol(symbol);
        
        Stock updatedStock = stockService.saveStock(stock);
        return ResponseEntity.ok(updatedStock);
    }

    /**
     * Deletes a stock by its symbol.
     * 
     * @param symbol the stock symbol
     * @return ResponseEntity with HTTP 204 status if deleted, 404 if not found
     */
    @DeleteMapping("/{symbol}")
    public ResponseEntity<Void> deleteStock(@PathVariable String symbol) {
        logger.info("DELETE /api/stocks/{} - Deleting stock", symbol);
        
        boolean deleted = stockService.deleteStock(symbol);
        
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Health check endpoint.
     * 
     * @return ResponseEntity with status message and HTTP 200 status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Stock Checker API",
            "version", "1.0.0"
        ));
    }
}
