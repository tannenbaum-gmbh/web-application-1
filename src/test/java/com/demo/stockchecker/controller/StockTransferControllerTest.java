package com.demo.stockchecker.controller;

import com.demo.stockchecker.exception.StockTransferNotFoundException;
import com.demo.stockchecker.model.StockTransferRequest;
import com.demo.stockchecker.service.StockTransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for StockTransferController.
 * Uses MockMvc to test REST endpoints without starting the full server.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
@WebMvcTest(StockTransferController.class)
class StockTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StockTransferService stockTransferService;

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
    void getAllTransferRequests_shouldReturnAllTransfers() throws Exception {
        // Arrange
        List<StockTransferRequest> transfers = Arrays.asList(testTransfer);
        when(stockTransferService.getAllTransferRequests()).thenReturn(transfers);

        // Act & Assert
        mockMvc.perform(get("/api/stock-transfers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].transferId", is("test-id-1")))
                .andExpect(jsonPath("$[0].stockSymbol", is("AAPL")))
                .andExpect(jsonPath("$[0].fromCountry", is("USA")))
                .andExpect(jsonPath("$[0].toCountry", is("DEU")));

        verify(stockTransferService, times(1)).getAllTransferRequests();
    }

    @Test
    void getTransferRequestsFromCountry_shouldReturnTransfers() throws Exception {
        // Arrange
        List<StockTransferRequest> transfers = Arrays.asList(testTransfer);
        when(stockTransferService.getTransferRequestsFromCountry("USA")).thenReturn(transfers);

        // Act & Assert
        mockMvc.perform(get("/api/stock-transfers/from/USA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].fromCountry", is("USA")));

        verify(stockTransferService, times(1)).getTransferRequestsFromCountry("USA");
    }

    @Test
    void getTransferRequestsToCountry_shouldReturnTransfers() throws Exception {
        // Arrange
        List<StockTransferRequest> transfers = Arrays.asList(testTransfer);
        when(stockTransferService.getTransferRequestsToCountry("DEU")).thenReturn(transfers);

        // Act & Assert
        mockMvc.perform(get("/api/stock-transfers/to/DEU"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].toCountry", is("DEU")));

        verify(stockTransferService, times(1)).getTransferRequestsToCountry("DEU");
    }

    @Test
    void getTransferRequestById_shouldReturnTransfer() throws Exception {
        // Arrange
        when(stockTransferService.getTransferRequestById("test-id-1")).thenReturn(testTransfer);

        // Act & Assert
        mockMvc.perform(get("/api/stock-transfers/test-id-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transferId", is("test-id-1")))
                .andExpect(jsonPath("$.stockSymbol", is("AAPL")));

        verify(stockTransferService, times(1)).getTransferRequestById("test-id-1");
    }

    @Test
    void getTransferRequestById_withInvalidId_shouldReturnNotFound() throws Exception {
        // Arrange
        when(stockTransferService.getTransferRequestById("invalid-id"))
                .thenThrow(new StockTransferNotFoundException("invalid-id"));

        // Act & Assert
        mockMvc.perform(get("/api/stock-transfers/invalid-id"))
                .andExpect(status().isNotFound());

        verify(stockTransferService, times(1)).getTransferRequestById("invalid-id");
    }

    @Test
    void createTransferRequest_shouldCreateTransfer() throws Exception {
        // Arrange
        when(stockTransferService.createTransferRequest(any(StockTransferRequest.class)))
                .thenReturn(testTransfer);

        // Act & Assert
        mockMvc.perform(post("/api/stock-transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTransfer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transferId", is("test-id-1")))
                .andExpect(jsonPath("$.stockSymbol", is("AAPL")))
                .andExpect(jsonPath("$.quantity", is(100)))
                .andExpect(jsonPath("$.fromCountry", is("USA")))
                .andExpect(jsonPath("$.toCountry", is("DEU")));

        verify(stockTransferService, times(1)).createTransferRequest(any(StockTransferRequest.class));
    }

    @Test
    void createTransferRequest_withInvalidData_shouldReturnBadRequest() throws Exception {
        // Arrange - create invalid transfer with missing required fields
        StockTransferRequest invalidTransfer = new StockTransferRequest();

        // Act & Assert
        mockMvc.perform(post("/api/stock-transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTransfer)))
                .andExpect(status().isBadRequest());

        verify(stockTransferService, never()).createTransferRequest(any(StockTransferRequest.class));
    }

    @Test
    void updateTransferStatus_shouldUpdateStatus() throws Exception {
        // Arrange
        testTransfer.setStatus("COMPLETED");
        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", "COMPLETED");
        
        when(stockTransferService.updateTransferStatus("test-id-1", "COMPLETED"))
                .thenReturn(testTransfer);

        // Act & Assert
        mockMvc.perform(patch("/api/stock-transfers/test-id-1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transferId", is("test-id-1")))
                .andExpect(jsonPath("$.status", is("COMPLETED")));

        verify(stockTransferService, times(1)).updateTransferStatus("test-id-1", "COMPLETED");
    }

    @Test
    void updateTransferStatus_withInvalidId_shouldReturnNotFound() throws Exception {
        // Arrange
        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", "COMPLETED");
        
        when(stockTransferService.updateTransferStatus("invalid-id", "COMPLETED"))
                .thenThrow(new StockTransferNotFoundException("invalid-id"));

        // Act & Assert
        mockMvc.perform(patch("/api/stock-transfers/invalid-id/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isNotFound());

        verify(stockTransferService, times(1)).updateTransferStatus("invalid-id", "COMPLETED");
    }

    @Test
    void deleteTransferRequest_shouldDeleteTransfer() throws Exception {
        // Arrange
        when(stockTransferService.deleteTransferRequest("test-id-1")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/stock-transfers/test-id-1"))
                .andExpect(status().isNoContent());

        verify(stockTransferService, times(1)).deleteTransferRequest("test-id-1");
    }

    @Test
    void deleteTransferRequest_withInvalidId_shouldReturnNotFound() throws Exception {
        // Arrange
        when(stockTransferService.deleteTransferRequest("invalid-id")).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/stock-transfers/invalid-id"))
                .andExpect(status().isNotFound());

        verify(stockTransferService, times(1)).deleteTransferRequest("invalid-id");
    }
}
