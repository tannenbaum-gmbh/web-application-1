package com.demo.stockchecker.exception;

/**
 * Exception thrown when a requested stock transfer is not found in the repository.
 * This is a runtime exception that will be handled by the global exception handler.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
public class StockTransferNotFoundException extends RuntimeException {

    private final String transferId;

    /**
     * Constructor with transfer ID.
     * 
     * @param transferId the transfer ID that was not found
     */
    public StockTransferNotFoundException(String transferId) {
        super(String.format("Stock transfer with ID '%s' not found", transferId));
        this.transferId = transferId;
    }

    /**
     * Constructor with custom message and transfer ID.
     * 
     * @param message the custom error message
     * @param transferId the transfer ID that was not found
     */
    public StockTransferNotFoundException(String message, String transferId) {
        super(message);
        this.transferId = transferId;
    }

    /**
     * Gets the transfer ID that was not found.
     * 
     * @return the transfer ID
     */
    public String getTransferId() {
        return transferId;
    }
}
