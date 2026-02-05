package com.demo.stockchecker.service;

import com.demo.stockchecker.model.StockTransferRequest;

import java.util.List;

/**
 * Service interface for stock transfer operations.
 * Defines business logic methods for managing stock transfers between countries.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
public interface StockTransferService {

    /**
     * Retrieves all stock transfer requests.
     * 
     * @return a list of all transfer requests
     */
    List<StockTransferRequest> getAllTransferRequests();

    /**
     * Retrieves transfer requests from a specific country.
     * 
     * @param country the source country code
     * @return a list of transfer requests from the specified country
     */
    List<StockTransferRequest> getTransferRequestsFromCountry(String country);

    /**
     * Retrieves transfer requests to a specific country.
     * 
     * @param country the destination country code
     * @return a list of transfer requests to the specified country
     */
    List<StockTransferRequest> getTransferRequestsToCountry(String country);

    /**
     * Retrieves a specific transfer request by its ID.
     * 
     * @param transferId the transfer ID
     * @return the transfer request
     * @throws com.demo.stockchecker.exception.StockTransferNotFoundException if transfer not found
     */
    StockTransferRequest getTransferRequestById(String transferId);

    /**
     * Creates a new stock transfer request to send stock to a country.
     * 
     * @param transferRequest the transfer request to create
     * @return the created transfer request
     * @throws IllegalArgumentException if transfer request data is invalid
     */
    StockTransferRequest createTransferRequest(StockTransferRequest transferRequest);

    /**
     * Updates the status of a transfer request.
     * 
     * @param transferId the transfer ID
     * @param status the new status
     * @return the updated transfer request
     * @throws com.demo.stockchecker.exception.StockTransferNotFoundException if transfer not found
     */
    StockTransferRequest updateTransferStatus(String transferId, String status);

    /**
     * Deletes a transfer request.
     * 
     * @param transferId the transfer ID
     * @return true if deleted, false if not found
     */
    boolean deleteTransferRequest(String transferId);
}
