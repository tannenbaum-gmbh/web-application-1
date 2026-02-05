package com.demo.stockchecker.controller;

import com.demo.stockchecker.model.StockTransferRequest;
import com.demo.stockchecker.service.StockTransferService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for stock transfer operations.
 * Provides endpoints for managing stock transfers between countries.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/stock-transfers")
@Validated
public class StockTransferController {

    private static final Logger logger = LoggerFactory.getLogger(StockTransferController.class);

    private final StockTransferService stockTransferService;

    /**
     * Constructor with dependency injection.
     * 
     * @param stockTransferService the stock transfer service
     */
    public StockTransferController(StockTransferService stockTransferService) {
        this.stockTransferService = stockTransferService;
    }

    /**
     * Retrieves all stock transfer requests.
     * 
     * @return ResponseEntity with list of all transfer requests and HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<StockTransferRequest>> getAllTransferRequests() {
        logger.info("GET /api/stock-transfers - Fetching all transfer requests");
        List<StockTransferRequest> transfers = stockTransferService.getAllTransferRequests();
        return ResponseEntity.ok(transfers);
    }

    /**
     * Retrieves transfer requests from a specific country.
     * 
     * @param country the source country code
     * @return ResponseEntity with list of transfer requests and HTTP 200 status
     */
    @GetMapping("/from/{country}")
    public ResponseEntity<List<StockTransferRequest>> getTransferRequestsFromCountry(
            @PathVariable String country) {
        logger.info("GET /api/stock-transfers/from/{} - Fetching transfer requests from country", country);
        List<StockTransferRequest> transfers = stockTransferService.getTransferRequestsFromCountry(country);
        return ResponseEntity.ok(transfers);
    }

    /**
     * Retrieves transfer requests to a specific country.
     * 
     * @param country the destination country code
     * @return ResponseEntity with list of transfer requests and HTTP 200 status
     */
    @GetMapping("/to/{country}")
    public ResponseEntity<List<StockTransferRequest>> getTransferRequestsToCountry(
            @PathVariable String country) {
        logger.info("GET /api/stock-transfers/to/{} - Fetching transfer requests to country", country);
        List<StockTransferRequest> transfers = stockTransferService.getTransferRequestsToCountry(country);
        return ResponseEntity.ok(transfers);
    }

    /**
     * Retrieves a specific transfer request by its ID.
     * 
     * @param transferId the transfer ID
     * @return ResponseEntity with the transfer request and HTTP 200 status
     * @throws com.demo.stockchecker.exception.StockTransferNotFoundException if transfer not found
     */
    @GetMapping("/{transferId}")
    public ResponseEntity<StockTransferRequest> getTransferRequestById(
            @PathVariable String transferId) {
        logger.info("GET /api/stock-transfers/{} - Fetching transfer request", transferId);
        StockTransferRequest transfer = stockTransferService.getTransferRequestById(transferId);
        return ResponseEntity.ok(transfer);
    }

    /**
     * Creates a new stock transfer request to send stock to a country.
     * 
     * @param transferRequest the transfer request data
     * @return ResponseEntity with created transfer request and HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<StockTransferRequest> createTransferRequest(
            @Valid @RequestBody StockTransferRequest transferRequest) {
        logger.info("POST /api/stock-transfers - Creating transfer request from {} to {}", 
                transferRequest.getFromCountry(), transferRequest.getToCountry());
        
        StockTransferRequest createdTransfer = stockTransferService.createTransferRequest(transferRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransfer);
    }

    /**
     * Updates the status of a transfer request.
     * 
     * @param transferId the transfer ID
     * @param statusUpdate map containing the new status
     * @return ResponseEntity with updated transfer request and HTTP 200 status
     * @throws com.demo.stockchecker.exception.StockTransferNotFoundException if transfer not found
     */
    @PatchMapping("/{transferId}/status")
    public ResponseEntity<StockTransferRequest> updateTransferStatus(
            @PathVariable String transferId,
            @RequestBody Map<String, String> statusUpdate) {
        
        logger.info("PATCH /api/stock-transfers/{}/status - Updating transfer status", transferId);
        
        String status = statusUpdate.get("status");
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status field is required in request body");
        }
        
        StockTransferRequest updatedTransfer = stockTransferService.updateTransferStatus(transferId, status);
        return ResponseEntity.ok(updatedTransfer);
    }

    /**
     * Deletes a transfer request by its ID.
     * 
     * @param transferId the transfer ID
     * @return ResponseEntity with HTTP 204 status if deleted, 404 if not found
     */
    @DeleteMapping("/{transferId}")
    public ResponseEntity<Void> deleteTransferRequest(@PathVariable String transferId) {
        logger.info("DELETE /api/stock-transfers/{} - Deleting transfer request", transferId);
        
        boolean deleted = stockTransferService.deleteTransferRequest(transferId);
        
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
