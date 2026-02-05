package com.demo.stockchecker.service;

import com.demo.stockchecker.exception.StockTransferNotFoundException;
import com.demo.stockchecker.model.StockTransferRequest;
import com.demo.stockchecker.repository.StockTransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of StockTransferService interface.
 * Provides business logic for stock transfer operations.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
@Service
public class StockTransferServiceImpl implements StockTransferService {

    private static final Logger logger = LoggerFactory.getLogger(StockTransferServiceImpl.class);

    private final StockTransferRepository stockTransferRepository;

    /**
     * Constructor with dependency injection.
     * 
     * @param stockTransferRepository the stock transfer repository
     */
    public StockTransferServiceImpl(StockTransferRepository stockTransferRepository) {
        this.stockTransferRepository = stockTransferRepository;
    }

    @Override
    public List<StockTransferRequest> getAllTransferRequests() {
        logger.debug("Fetching all transfer requests");
        List<StockTransferRequest> transfers = stockTransferRepository.findAll();
        logger.info("Retrieved {} transfer requests", transfers.size());
        return transfers;
    }

    @Override
    public List<StockTransferRequest> getTransferRequestsFromCountry(String country) {
        logger.debug("Fetching transfer requests from country: {}", country);
        
        if (country == null || country.trim().isEmpty()) {
            logger.error("Country is null or empty");
            throw new IllegalArgumentException("Country cannot be null or empty");
        }
        
        List<StockTransferRequest> transfers = stockTransferRepository.findByFromCountry(country);
        logger.info("Retrieved {} transfer requests from country: {}", transfers.size(), country);
        return transfers;
    }

    @Override
    public List<StockTransferRequest> getTransferRequestsToCountry(String country) {
        logger.debug("Fetching transfer requests to country: {}", country);
        
        if (country == null || country.trim().isEmpty()) {
            logger.error("Country is null or empty");
            throw new IllegalArgumentException("Country cannot be null or empty");
        }
        
        List<StockTransferRequest> transfers = stockTransferRepository.findByToCountry(country);
        logger.info("Retrieved {} transfer requests to country: {}", transfers.size(), country);
        return transfers;
    }

    @Override
    public StockTransferRequest getTransferRequestById(String transferId) {
        logger.debug("Fetching transfer request with ID: {}", transferId);
        
        if (transferId == null || transferId.trim().isEmpty()) {
            logger.error("Transfer ID is null or empty");
            throw new IllegalArgumentException("Transfer ID cannot be null or empty");
        }
        
        return stockTransferRepository.findByTransferId(transferId)
                .orElseThrow(() -> {
                    logger.error("Transfer request not found: {}", transferId);
                    return new StockTransferNotFoundException(transferId);
                });
    }

    @Override
    public StockTransferRequest createTransferRequest(StockTransferRequest transferRequest) {
        if (transferRequest == null) {
            logger.error("Transfer request object is null");
            throw new IllegalArgumentException("Transfer request cannot be null");
        }
        
        logger.debug("Creating transfer request for stock: {}", transferRequest.getStockSymbol());
        
        // Validation
        if (transferRequest.getStockSymbol() == null || transferRequest.getStockSymbol().trim().isEmpty()) {
            logger.error("Stock symbol is null or empty");
            throw new IllegalArgumentException("Stock symbol cannot be null or empty");
        }
        
        if (transferRequest.getQuantity() == null || transferRequest.getQuantity() <= 0) {
            logger.error("Invalid quantity: {}", transferRequest.getQuantity());
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        if (transferRequest.getFromCountry() == null || transferRequest.getFromCountry().trim().isEmpty()) {
            logger.error("From country is null or empty");
            throw new IllegalArgumentException("From country cannot be null or empty");
        }
        
        if (transferRequest.getToCountry() == null || transferRequest.getToCountry().trim().isEmpty()) {
            logger.error("To country is null or empty");
            throw new IllegalArgumentException("To country cannot be null or empty");
        }
        
        // Ensure stock symbol is uppercase
        transferRequest.setStockSymbol(transferRequest.getStockSymbol().toUpperCase());
        
        // Set initial status if not provided
        if (transferRequest.getStatus() == null || transferRequest.getStatus().trim().isEmpty()) {
            transferRequest.setStatus("PENDING");
        }
        
        StockTransferRequest savedTransfer = stockTransferRepository.save(transferRequest);
        logger.info("Created transfer request: {} from {} to {}", 
                savedTransfer.getTransferId(), savedTransfer.getFromCountry(), savedTransfer.getToCountry());
        
        return savedTransfer;
    }

    @Override
    public StockTransferRequest updateTransferStatus(String transferId, String status) {
        logger.debug("Updating status for transfer: {} to {}", transferId, status);
        
        if (transferId == null || transferId.trim().isEmpty()) {
            logger.error("Transfer ID is null or empty");
            throw new IllegalArgumentException("Transfer ID cannot be null or empty");
        }
        
        if (status == null || status.trim().isEmpty()) {
            logger.error("Status is null or empty");
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        
        StockTransferRequest transfer = getTransferRequestById(transferId);
        transfer.setStatus(status);
        
        StockTransferRequest updatedTransfer = stockTransferRepository.save(transfer);
        logger.info("Updated transfer: {} to status: {}", transferId, status);
        
        return updatedTransfer;
    }

    @Override
    public boolean deleteTransferRequest(String transferId) {
        logger.debug("Deleting transfer request: {}", transferId);
        
        if (transferId == null || transferId.trim().isEmpty()) {
            logger.error("Transfer ID is null or empty");
            throw new IllegalArgumentException("Transfer ID cannot be null or empty");
        }
        
        boolean deleted = stockTransferRepository.deleteByTransferId(transferId);
        
        if (deleted) {
            logger.info("Deleted transfer request: {}", transferId);
        } else {
            logger.warn("Transfer request not found for deletion: {}", transferId);
        }
        
        return deleted;
    }
}
