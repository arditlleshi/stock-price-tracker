package com.stockpricetracker.stockpricetracker;

import com.stockpricetracker.stockpricetracker.config.AlphaVantageConfig;
import com.stockpricetracker.stockpricetracker.exceptions.StockAlreadyExistsException;
import com.stockpricetracker.stockpricetracker.model.Stock;
import com.stockpricetracker.stockpricetracker.repository.StockRepository;
import com.stockpricetracker.stockpricetracker.service.ApiRequestService;
import com.stockpricetracker.stockpricetracker.service.implementation.StockServiceImplementation;
import com.stockpricetracker.stockpricetracker.util.AlphaVantageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class StockServiceImplementationTest {
    @Mock
    private StockRepository stockRepository;
    @Mock
    private AlphaVantageConfig alphaVantageConfig;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ApiRequestService apiRequestService;
    private StockServiceImplementation stockServiceImplementation;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        stockServiceImplementation = new StockServiceImplementation(stockRepository, restTemplate, alphaVantageConfig, apiRequestService);
    }

    @Test
    public void testAddStock_Success(){
        Stock stock = new Stock();
        stock.setId(1L);
        stock.setSymbol("AAPL");

        AlphaVantageResponse response = new AlphaVantageResponse();
        response.setGlobalQuote(new AlphaVantageResponse.GlobalQuote());
        response.getGlobalQuote().setSymbol("AAPL");

        ResponseEntity<AlphaVantageResponse> responseEntity = ResponseEntity.ok(response);

        when(stockRepository.findBySymbol("AAPL")).thenReturn(Optional.empty());
        when(alphaVantageConfig.getBaseUrl()).thenReturn("https://www.alphavantage.co");
        when(alphaVantageConfig.getApiKey()).thenReturn("EWKLCPEY3WK5BMJO");
        when(restTemplate.getForEntity(anyString(), eq(AlphaVantageResponse.class))).thenReturn(responseEntity);
        when(stockRepository.save(stock)).thenReturn(stock);

        Stock addedStock = stockServiceImplementation.addStock(stock);

        assertNotNull(addedStock);
        assertEquals("AAPL", addedStock.getSymbol());
        verify(stockRepository).findBySymbol("AAPL");
        verify(alphaVantageConfig).getBaseUrl();
        verify(alphaVantageConfig).getApiKey();
        verify(restTemplate).getForEntity(anyString(), eq(AlphaVantageResponse.class));
        verify(stockRepository).save(stock);
    }

    @Test
    public void testAddStock_StockAlreadyExists(){
        Stock stock = new Stock();
        stock.setSymbol("AAPL");

        when(stockRepository.findBySymbol("AAPL")).thenReturn(Optional.of(stock));

        assertThrows(StockAlreadyExistsException.class, () -> stockServiceImplementation.addStock(stock));
        verify(stockRepository).findBySymbol("AAPL");
        verifyNoMoreInteractions(stockRepository, alphaVantageConfig, restTemplate);
    }
}
