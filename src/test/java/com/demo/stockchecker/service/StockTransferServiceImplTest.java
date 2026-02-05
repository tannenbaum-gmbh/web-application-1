package com.demo.stockchecker.service;

import com.demo.stockchecker.exception.StockTransferNotFoundException;
import com.demo.stockchecker.model.StockTransferRequest;
import com.demo.stockchecker.repository.StockTransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for StockTransferServiceImpl.
 * Tests business logic without starting the Spring context.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class StockTransferServiceImplTest {

    @Mock
    private StockTransferRepository stockTransferRepository;

    @InjectMocks
    private StockTransferServiceImpl stockTransferService;

    private StockTransferRequest testTransfer;

    @BeforeEach
    void setUp() {
        testTransfer = new StockTransferRequest(
            "test-id-1",
            "AAPL",
            100L,
            "USA",
            "DEU"
        );
    }

    @Test
    void getAllTransferRequests_shouldReturnAllTransfers() {
        // Arrange
        List<StockTransferRequest> transfers = Arrays.asList(testTransfer);
        when(stockTransferRepository.findAll()).thenReturn(transfers);

        // Act
        List<StockTransferRequest> result = stockTransferService.getAllTransferRequests();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testTransfer, result.get(0));
        verify(stockTransferRepository, times(1)).findAll();
    }

    @Test
    void getTransferRequestsFromCountry_shouldReturnTransfersFromCountry() {
        // Arrange
        List<StockTransferRequest> transfers = Arrays.asList(testTransfer);
        when(stockTransferRepository.findByFromCountry("USA")).thenReturn(transfers);

        // Act
        List<StockTransferRequest> result = stockTransferService.getTransferRequestsFromCountry("USA");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("USA", result.get(0).getFromCountry());
        verify(stockTransferRepository, times(1)).findByFromCountry("USA");
    }

    @Test
    void getTransferRequestsFromCountry_withNullCountry_shouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> stockTransferService.getTransferRequestsFromCountry(null));
    }

    @Test
    void getTransferRequestsFromCountry_withEmptyCountry_shouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> stockTransferService.getTransferRequestsFromCountry(""));
    }

    @Test
    void getTransferRequestsToCountry_shouldReturnTransfersToCountry() {
        // Arrange
        List<StockTransferRequest> transfers = Arrays.asList(testTransfer);
        when(stockTransferRepository.findByToCountry("DEU")).thenReturn(transfers);

        // Act
        List<StockTransferRequest> result = stockTransferService.getTransferRequestsToCountry("DEU");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("DEU", result.get(0).getToCountry());
        verify(stockTransferRepository, times(1)).findByToCountry("DEU");
    }

    @Test
    void getTransferRequestsToCountry_withNullCountry_shouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> stockTransferService.getTransferRequestsToCountry(null));
    }

    @Test
    void getTransferRequestById_shouldReturnTransfer() {
        // Arrange
        when(stockTransferRepository.findByTransferId("test-id-1")).thenReturn(Optional.of(testTransfer));

        // Act
        StockTransferRequest result = stockTransferService.getTransferRequestById("test-id-1");

        // Assert
        assertNotNull(result);
        assertEquals("test-id-1", result.getTransferId());
        verify(stockTransferRepository, times(1)).findByTransferId("test-id-1");
    }

    @Test
    void getTransferRequestById_withInvalidId_shouldThrowNotFoundException() {
        // Arrange
        when(stockTransferRepository.findByTransferId("invalid-id")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(StockTransferNotFoundException.class, 
            () -> stockTransferService.getTransferRequestById("invalid-id"));
    }

    @Test
    void getTransferRequestById_withNullId_shouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> stockTransferService.getTransferRequestById(null));
    }

    @Test
    void createTransferRequest_shouldCreateNewTransfer() {
        // Arrange
        when(stockTransferRepository.save(any(StockTransferRequest.class))).thenReturn(testTransfer);

        // Act
        StockTransferRequest result = stockTransferService.createTransferRequest(testTransfer);

        // Assert
        assertNotNull(result);
        assertEquals("AAPL", result.getStockSymbol());
        assertEquals("USA", result.getFromCountry());
        assertEquals("DEU", result.getToCountry());
        verify(stockTransferRepository, times(1)).save(any(StockTransferRequest.class));
    }

    @Test
    void createTransferRequest_withNullTransfer_shouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> stockTransferService.createTransferRequest(null));
    }

    @Test
    void createTransferRequest_withInvalidQuantity_shouldThrowException() {
        // Arrange
        testTransfer.setQuantity(-1L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> stockTransferService.createTransferRequest(testTransfer));
    }

    @Test
    void createTransferRequest_withNullFromCountry_shouldThrowException() {
        // Arrange
        testTransfer.setFromCountry(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> stockTransferService.createTransferRequest(testTransfer));
    }

    @Test
    void createTransferRequest_withNullToCountry_shouldThrowException() {
        // Arrange
        testTransfer.setToCountry(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> stockTransferService.createTransferRequest(testTransfer));
    }

    @Test
    void updateTransferStatus_shouldUpdateStatus() {
        // Arrange
        when(stockTransferRepository.findByTransferId("test-id-1")).thenReturn(Optional.of(testTransfer));
        when(stockTransferRepository.save(any(StockTransferRequest.class))).thenReturn(testTransfer);

        // Act
        StockTransferRequest result = stockTransferService.updateTransferStatus("test-id-1", "COMPLETED");

        // Assert
        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
        verify(stockTransferRepository, times(1)).findByTransferId("test-id-1");
        verify(stockTransferRepository, times(1)).save(any(StockTransferRequest.class));
    }

    @Test
    void updateTransferStatus_withNullId_shouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> stockTransferService.updateTransferStatus(null, "COMPLETED"));
    }

    @Test
    void updateTransferStatus_withNullStatus_shouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> stockTransferService.updateTransferStatus("test-id-1", null));
    }

    @Test
    void deleteTransferRequest_shouldDeleteTransfer() {
        // Arrange
        when(stockTransferRepository.deleteByTransferId("test-id-1")).thenReturn(true);

        // Act
        boolean result = stockTransferService.deleteTransferRequest("test-id-1");

        // Assert
        assertTrue(result);
        verify(stockTransferRepository, times(1)).deleteByTransferId("test-id-1");
    }

    @Test
    void deleteTransferRequest_withInvalidId_shouldReturnFalse() {
        // Arrange
        when(stockTransferRepository.deleteByTransferId("invalid-id")).thenReturn(false);

        // Act
        boolean result = stockTransferService.deleteTransferRequest("invalid-id");

        // Assert
        assertFalse(result);
        verify(stockTransferRepository, times(1)).deleteByTransferId("invalid-id");
    }

    @Test
    void deleteTransferRequest_withNullId_shouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> stockTransferService.deleteTransferRequest(null));
    }
}
