package com.demo.stockchecker.repository;

import com.demo.stockchecker.model.StockTransferRequest;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Repository for managing stock transfer requests.
 * Uses an in-memory data store (ConcurrentHashMap) for demo purposes.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
@Repository
public class StockTransferRepository {

    private final Map<String, StockTransferRequest> transferStore = new ConcurrentHashMap<>();

    /**
     * Retrieves all stock transfer requests.
     * 
     * @return a list of all transfer requests
     */
    public List<StockTransferRequest> findAll() {
        return new ArrayList<>(transferStore.values());
    }

    /**
     * Retrieves transfer requests by country (from or to).
     * 
     * @param country the country code
     * @return a list of transfer requests involving the specified country
     */
    public List<StockTransferRequest> findByCountry(String country) {
        return transferStore.values().stream()
                .filter(transfer -> transfer.getFromCountry().equalsIgnoreCase(country) 
                        || transfer.getToCountry().equalsIgnoreCase(country))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves transfer requests from a specific country.
     * 
     * @param country the source country code
     * @return a list of transfer requests from the specified country
     */
    public List<StockTransferRequest> findByFromCountry(String country) {
        return transferStore.values().stream()
                .filter(transfer -> transfer.getFromCountry().equalsIgnoreCase(country))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves transfer requests to a specific country.
     * 
     * @param country the destination country code
     * @return a list of transfer requests to the specified country
     */
    public List<StockTransferRequest> findByToCountry(String country) {
        return transferStore.values().stream()
                .filter(transfer -> transfer.getToCountry().equalsIgnoreCase(country))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a transfer request by its ID.
     * 
     * @param transferId the transfer ID
     * @return an Optional containing the transfer request if found
     */
    public Optional<StockTransferRequest> findByTransferId(String transferId) {
        return Optional.ofNullable(transferStore.get(transferId));
    }

    /**
     * Saves a transfer request.
     * 
     * @param transferRequest the transfer request to save
     * @return the saved transfer request
     */
    public StockTransferRequest save(StockTransferRequest transferRequest) {
        if (transferRequest.getTransferId() == null || transferRequest.getTransferId().isEmpty()) {
            transferRequest.setTransferId(UUID.randomUUID().toString());
        }
        transferStore.put(transferRequest.getTransferId(), transferRequest);
        return transferRequest;
    }

    /**
     * Deletes a transfer request by its ID.
     * 
     * @param transferId the transfer ID
     * @return true if deleted, false if not found
     */
    public boolean deleteByTransferId(String transferId) {
        return transferStore.remove(transferId) != null;
    }

    /**
     * Checks if a transfer request exists by its ID.
     * 
     * @param transferId the transfer ID
     * @return true if exists, false otherwise
     */
    public boolean existsByTransferId(String transferId) {
        return transferStore.containsKey(transferId);
    }
}
