package com.demo.stockchecker.service;

import com.demo.stockchecker.exception.StockNotFoundException;
import com.demo.stockchecker.model.Stock;
import com.demo.stockchecker.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of StockService interface.
 * Provides business logic for stock operations.
 * 
 * @author Demo Team
 * @version 2.0.0
 */
@Service
public class StockServiceImpl implements StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

    private final StockRepository stockRepository;

    /**
     * Constructor with dependency injection.
     * 
     * @param stockRepository the stock repository
     */
    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public List<Stock> getAllStocks() {
        logger.debug("Fetching all stocks");
        List<Stock> stocks = stockRepository.findAll();
        logger.info("Retrieved {} stocks", stocks.size());
        return stocks;
    }

    @Override
    public Stock getStockBySymbol(String symbol) {
        logger.debug("Fetching stock with symbol: {}", symbol);
        
        if (symbol == null || symbol.trim().isEmpty()) {
            logger.error("Stock symbol is null or empty");
            throw new IllegalArgumentException("Stock symbol cannot be null or empty");
        }
        
        return stockRepository.findBySymbolIgnoreCase(symbol)
                .orElseThrow(() -> {
                    logger.error("Stock not found: {}", symbol);
                    return new StockNotFoundException(symbol.toUpperCase());
                });
    }

    @Override
    @Transactional
    public Stock updateStockPrice(String symbol, BigDecimal newPrice) {
        logger.debug("Updating price for stock: {} to {}", symbol, newPrice);
        
        // Validation
        if (symbol == null || symbol.trim().isEmpty()) {
            logger.error("Stock symbol is null or empty");
            throw new IllegalArgumentException("Stock symbol cannot be null or empty");
        }
        
        if (newPrice == null) {
            logger.error("New price is null");
            throw new IllegalArgumentException("New price cannot be null");
        }
        
        if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("New price is not positive: {}", newPrice);
            throw new IllegalArgumentException("New price must be positive");
        }
        
        // Fetch existing stock
        Stock stock = getStockBySymbol(symbol);
        BigDecimal oldPrice = stock.getCurrentPrice();
        
        // Calculate change and change percent
        BigDecimal change = newPrice.subtract(oldPrice);
        BigDecimal changePercent = BigDecimal.ZERO;
        
        if (oldPrice.compareTo(BigDecimal.ZERO) != 0) {
            changePercent = change.divide(oldPrice, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))
                    .setScale(2, RoundingMode.HALF_UP);
        }
        
        // Update stock
        stock.setCurrentPrice(newPrice);
        stock.setChange(change.setScale(2, RoundingMode.HALF_UP));
        stock.setChangePercent(changePercent);
        stock.setLastUpdated(LocalDateTime.now());
        
        Stock updatedStock = stockRepository.save(stock);
        logger.info("Updated stock: {} from {} to {}", symbol, oldPrice, newPrice);
        
        return updatedStock;
    }

    @Override
    @Transactional
    public Stock saveStock(Stock stock) {
        if (stock == null) {
            logger.error("Stock object is null");
            throw new IllegalArgumentException("Stock cannot be null");
        }
        
        logger.debug("Saving stock: {}", stock.getSymbol());
        
        if (stock.getSymbol() == null || stock.getSymbol().trim().isEmpty()) {
            logger.error("Stock symbol is null or empty");
            throw new IllegalArgumentException("Stock symbol cannot be null or empty");
        }
        
        // Ensure symbol is uppercase
        stock.setSymbol(stock.getSymbol().toUpperCase());
        
        Stock savedStock = stockRepository.save(stock);
        logger.info("Saved stock: {}", savedStock.getSymbol());
        
        return savedStock;
    }

    @Override
    @Transactional
    public boolean deleteStock(String symbol) {
        logger.debug("Deleting stock: {}", symbol);
        
        if (symbol == null || symbol.trim().isEmpty()) {
            logger.error("Stock symbol is null or empty");
            throw new IllegalArgumentException("Stock symbol cannot be null or empty");
        }
        
        String upperSymbol = symbol.toUpperCase();
        if (stockRepository.existsBySymbolIgnoreCase(upperSymbol)) {
            stockRepository.deleteBySymbol(upperSymbol);
            logger.info("Deleted stock: {}", symbol);
            return true;
        } else {
            logger.warn("Stock not found for deletion: {}", symbol);
            return false;
        }
    }

    @Override
    public boolean stockExists(String symbol) {
        logger.debug("Checking if stock exists: {}", symbol);
        
        if (symbol == null || symbol.trim().isEmpty()) {
            return false;
        }
        
        return stockRepository.existsBySymbolIgnoreCase(symbol);
    }
}
