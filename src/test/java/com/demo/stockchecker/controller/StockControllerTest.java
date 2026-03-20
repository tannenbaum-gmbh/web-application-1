package com.demo.stockchecker.controller;

import com.demo.stockchecker.exception.StockNotFoundException;
import com.demo.stockchecker.model.Stock;
import com.demo.stockchecker.service.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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
 * Integration tests for StockController.
 * Uses MockMvc to test REST endpoints without starting the full server.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
@WebMvcTest(StockController.class)
@Import(TestSecurityConfig.class)
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StockService stockService;

    private Stock testStock;

    @BeforeEach
    void setUp() {
        testStock = new Stock(
            "AAPL",
            "Apple Inc.",
            new BigDecimal("150.00"),
            new BigDecimal("2.50"),
            new BigDecimal("1.69")
        );
    }

    @Test
    void getAllStocks_ShouldReturnListOfStocks() throws Exception {
        // Arrange
        List<Stock> stocks = Arrays.asList(testStock);
        when(stockService.getAllStocks()).thenReturn(stocks);

        // Act & Assert
        mockMvc.perform(get("/api/stocks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].symbol", is("AAPL")))
                .andExpect(jsonPath("$[0].name", is("Apple Inc.")))
                .andExpect(jsonPath("$[0].currentPrice", is(150.00)));

        verify(stockService, times(1)).getAllStocks();
    }

    @Test
    void getStockBySymbol_WhenStockExists_ShouldReturnStock() throws Exception {
        // Arrange
        when(stockService.getStockBySymbol("AAPL")).thenReturn(testStock);

        // Act & Assert
        mockMvc.perform(get("/api/stocks/AAPL"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.symbol", is("AAPL")))
                .andExpect(jsonPath("$.name", is("Apple Inc.")))
                .andExpect(jsonPath("$.currentPrice", is(150.00)));

        verify(stockService, times(1)).getStockBySymbol("AAPL");
    }

    @Test
    void getStockBySymbol_WhenStockNotFound_ShouldReturn404() throws Exception {
        // Arrange
        when(stockService.getStockBySymbol("INVALID")).thenThrow(new StockNotFoundException("INVALID"));

        // Act & Assert
        mockMvc.perform(get("/api/stocks/INVALID"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("INVALID")));

        verify(stockService, times(1)).getStockBySymbol("INVALID");
    }

    @Test
    void updateStockPrice_WhenValidPrice_ShouldReturnUpdatedStock() throws Exception {
        // Arrange
        Stock updatedStock = new Stock(
            "AAPL",
            "Apple Inc.",
            new BigDecimal("160.00"),
            new BigDecimal("12.50"),
            new BigDecimal("8.47")
        );
        when(stockService.updateStockPrice(anyString(), any(BigDecimal.class))).thenReturn(updatedStock);

        Map<String, BigDecimal> priceUpdate = new HashMap<>();
        priceUpdate.put("price", new BigDecimal("160.00"));

        // Act & Assert
        mockMvc.perform(put("/api/stocks/AAPL/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(priceUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol", is("AAPL")))
                .andExpect(jsonPath("$.currentPrice", is(160.00)));

        verify(stockService, times(1)).updateStockPrice(eq("AAPL"), any(BigDecimal.class));
    }

    @Test
    void updateStockPrice_WhenPriceMissing_ShouldReturn400() throws Exception {
        // Arrange
        Map<String, String> emptyUpdate = new HashMap<>();

        // Act & Assert
        mockMvc.perform(put("/api/stocks/AAPL/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyUpdate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));

        verify(stockService, never()).updateStockPrice(anyString(), any(BigDecimal.class));
    }

    @Test
    void createStock_WhenValidStock_ShouldReturn201() throws Exception {
        // Arrange
        when(stockService.stockExists(anyString())).thenReturn(false);
        when(stockService.saveStock(any(Stock.class))).thenReturn(testStock);

        // Act & Assert
        mockMvc.perform(post("/api/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStock)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.symbol", is("AAPL")))
                .andExpect(jsonPath("$.name", is("Apple Inc.")));

        verify(stockService, times(1)).saveStock(any(Stock.class));
    }

    @Test
    void createStock_WhenStockExists_ShouldReturn200() throws Exception {
        // Arrange
        when(stockService.stockExists(anyString())).thenReturn(true);
        when(stockService.saveStock(any(Stock.class))).thenReturn(testStock);

        // Act & Assert
        mockMvc.perform(post("/api/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol", is("AAPL")));

        verify(stockService, times(1)).saveStock(any(Stock.class));
    }

    @Test
    void createStock_WhenInvalidStock_ShouldReturn400() throws Exception {
        // Arrange - Create stock with missing required fields
        Stock invalidStock = new Stock();
        invalidStock.setSymbol("");  // Invalid: empty symbol

        // Act & Assert
        mockMvc.perform(post("/api/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidStock)))
                .andExpect(status().isBadRequest());

        verify(stockService, never()).saveStock(any(Stock.class));
    }

    @Test
    void updateStock_WhenValidStock_ShouldReturnUpdatedStock() throws Exception {
        // Arrange
        when(stockService.getStockBySymbol("AAPL")).thenReturn(testStock);
        when(stockService.saveStock(any(Stock.class))).thenReturn(testStock);

        // Act & Assert
        mockMvc.perform(put("/api/stocks/AAPL")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol", is("AAPL")));

        verify(stockService, times(1)).getStockBySymbol("AAPL");
        verify(stockService, times(1)).saveStock(any(Stock.class));
    }

    @Test
    void updateStock_WhenStockNotFound_ShouldReturn404() throws Exception {
        // Arrange
        when(stockService.getStockBySymbol("INVALID")).thenThrow(new StockNotFoundException("INVALID"));

        // Act & Assert
        mockMvc.perform(put("/api/stocks/INVALID")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStock)))
                .andExpect(status().isNotFound());

        verify(stockService, times(1)).getStockBySymbol("INVALID");
        verify(stockService, never()).saveStock(any(Stock.class));
    }

    @Test
    void deleteStock_WhenStockExists_ShouldReturn204() throws Exception {
        // Arrange
        when(stockService.deleteStock("AAPL")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/stocks/AAPL"))
                .andExpect(status().isNoContent());

        verify(stockService, times(1)).deleteStock("AAPL");
    }

    @Test
    void deleteStock_WhenStockNotFound_ShouldReturn404() throws Exception {
        // Arrange
        when(stockService.deleteStock("INVALID")).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/stocks/INVALID"))
                .andExpect(status().isNotFound());

        verify(stockService, times(1)).deleteStock("INVALID");
    }

    @Test
    void health_ShouldReturnHealthStatus() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/stocks/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.service", is("Stock Checker API")))
                .andExpect(jsonPath("$.version", is("1.0.0")));
    }
}
