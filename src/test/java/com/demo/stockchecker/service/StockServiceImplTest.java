package com.demo.stockchecker.service;

import com.demo.stockchecker.exception.StockNotFoundException;
import com.demo.stockchecker.model.Stock;
import com.demo.stockchecker.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for StockServiceImpl.
 * Uses Mockito to mock dependencies and test service logic in isolation.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class StockServiceImplTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockServiceImpl stockService;

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
    void getAllStocks_ShouldReturnAllStocks() {
        // Arrange
        List<Stock> expectedStocks = Arrays.asList(testStock);
        when(stockRepository.findAll()).thenReturn(expectedStocks);

        // Act
        List<Stock> actualStocks = stockService.getAllStocks();

        // Assert
        assertNotNull(actualStocks);
        assertEquals(1, actualStocks.size());
        assertEquals("AAPL", actualStocks.get(0).getSymbol());
        verify(stockRepository, times(1)).findAll();
    }

    @Test
    void getStockBySymbol_WhenStockExists_ShouldReturnStock() {
        // Arrange
        when(stockRepository.findBySymbol("AAPL")).thenReturn(Optional.of(testStock));

        // Act
        Stock actualStock = stockService.getStockBySymbol("AAPL");

        // Assert
        assertNotNull(actualStock);
        assertEquals("AAPL", actualStock.getSymbol());
        assertEquals("Apple Inc.", actualStock.getName());
        verify(stockRepository, times(1)).findBySymbol("AAPL");
    }

    @Test
    void getStockBySymbol_WhenStockNotFound_ShouldThrowException() {
        // Arrange
        when(stockRepository.findBySymbol("INVALID")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(StockNotFoundException.class, () -> {
            stockService.getStockBySymbol("INVALID");
        });
        verify(stockRepository, times(1)).findBySymbol("INVALID");
    }

    @Test
    void getStockBySymbol_WhenSymbolIsNull_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            stockService.getStockBySymbol(null);
        });
        verify(stockRepository, never()).findBySymbol(anyString());
    }

    @Test
    void getStockBySymbol_WhenSymbolIsEmpty_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            stockService.getStockBySymbol("");
        });
        verify(stockRepository, never()).findBySymbol(anyString());
    }

    @Test
    void updateStockPrice_WhenValidPrice_ShouldUpdateAndReturnStock() {
        // Arrange
        BigDecimal newPrice = new BigDecimal("160.00");
        when(stockRepository.findBySymbol("AAPL")).thenReturn(Optional.of(testStock));
        when(stockRepository.save(any(Stock.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Stock updatedStock = stockService.updateStockPrice("AAPL", newPrice);

        // Assert
        assertNotNull(updatedStock);
        assertEquals(newPrice, updatedStock.getCurrentPrice());
        assertEquals(new BigDecimal("10.00"), updatedStock.getChange());
        assertEquals(new BigDecimal("6.67"), updatedStock.getChangePercent());
        verify(stockRepository, times(1)).findBySymbol("AAPL");
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    void updateStockPrice_WhenPriceIsNull_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            stockService.updateStockPrice("AAPL", null);
        });
        verify(stockRepository, never()).save(any(Stock.class));
    }

    @Test
    void updateStockPrice_WhenPriceIsNegative_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            stockService.updateStockPrice("AAPL", new BigDecimal("-10.00"));
        });
        verify(stockRepository, never()).save(any(Stock.class));
    }

    @Test
    void updateStockPrice_WhenPriceIsZero_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            stockService.updateStockPrice("AAPL", BigDecimal.ZERO);
        });
        verify(stockRepository, never()).save(any(Stock.class));
    }

    @Test
    void saveStock_WhenValidStock_ShouldSaveAndReturnStock() {
        // Arrange
        when(stockRepository.save(any(Stock.class))).thenReturn(testStock);

        // Act
        Stock savedStock = stockService.saveStock(testStock);

        // Assert
        assertNotNull(savedStock);
        assertEquals("AAPL", savedStock.getSymbol());
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    void saveStock_WhenStockIsNull_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            stockService.saveStock(null);
        });
        verify(stockRepository, never()).save(any(Stock.class));
    }

    @Test
    void saveStock_ShouldConvertSymbolToUpperCase() {
        // Arrange
        Stock lowercaseStock = new Stock(
            "aapl",
            "Apple Inc.",
            new BigDecimal("150.00"),
            new BigDecimal("2.50"),
            new BigDecimal("1.69")
        );
        when(stockRepository.save(any(Stock.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Stock savedStock = stockService.saveStock(lowercaseStock);

        // Assert
        assertEquals("AAPL", savedStock.getSymbol());
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    void deleteStock_WhenStockExists_ShouldReturnTrue() {
        // Arrange
        when(stockRepository.deleteBySymbol("AAPL")).thenReturn(true);

        // Act
        boolean result = stockService.deleteStock("AAPL");

        // Assert
        assertTrue(result);
        verify(stockRepository, times(1)).deleteBySymbol("AAPL");
    }

    @Test
    void deleteStock_WhenStockNotFound_ShouldReturnFalse() {
        // Arrange
        when(stockRepository.deleteBySymbol("INVALID")).thenReturn(false);

        // Act
        boolean result = stockService.deleteStock("INVALID");

        // Assert
        assertFalse(result);
        verify(stockRepository, times(1)).deleteBySymbol("INVALID");
    }

    @Test
    void stockExists_WhenStockExists_ShouldReturnTrue() {
        // Arrange
        when(stockRepository.existsBySymbol("AAPL")).thenReturn(true);

        // Act
        boolean result = stockService.stockExists("AAPL");

        // Assert
        assertTrue(result);
        verify(stockRepository, times(1)).existsBySymbol("AAPL");
    }

    @Test
    void stockExists_WhenStockNotFound_ShouldReturnFalse() {
        // Arrange
        when(stockRepository.existsBySymbol("INVALID")).thenReturn(false);

        // Act
        boolean result = stockService.stockExists("INVALID");

        // Assert
        assertFalse(result);
        verify(stockRepository, times(1)).existsBySymbol("INVALID");
    }

    @Test
    void stockExists_WhenSymbolIsNull_ShouldReturnFalse() {
        // Act
        boolean result = stockService.stockExists(null);

        // Assert
        assertFalse(result);
        verify(stockRepository, never()).existsBySymbol(anyString());
    }
}
