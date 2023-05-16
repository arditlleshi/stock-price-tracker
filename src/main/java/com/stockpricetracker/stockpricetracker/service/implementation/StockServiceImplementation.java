package com.stockpricetracker.stockpricetracker.service.implementation;

import com.stockpricetracker.stockpricetracker.config.AlphaVantageConfig;
import com.stockpricetracker.stockpricetracker.model.Stock;
import com.stockpricetracker.stockpricetracker.repository.StockRepository;
import com.stockpricetracker.stockpricetracker.service.StockService;
import com.stockpricetracker.stockpricetracker.util.AlphaVantageResponse;
import com.stockpricetracker.stockpricetracker.util.StockDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImplementation implements StockService {
    private final StockRepository stockRepository;
    private final RestTemplate restTemplate;
    private final AlphaVantageConfig alphaVantageConfig;

    @Override
    public Stock addStock(Stock stock){
        String apiUrl = alphaVantageConfig.getBaseUrl() + "/query?function=GLOBAL_QUOTE&symbol=" + stock.getSymbol() + "&apikey=" + alphaVantageConfig.getApiKey();

        ResponseEntity<AlphaVantageResponse> responseEntity = restTemplate.getForEntity(apiUrl, AlphaVantageResponse.class);
        AlphaVantageResponse response = responseEntity.getBody();

        if (response != null && response.getGlobalQuote().getSymbol() != null) {
            stock.setSymbol(stock.getSymbol().toUpperCase());
            stockRepository.save(stock);
            return stock;
        } else {
            throw new RuntimeException("Unable to fetch stock price for symbol: " + stock.getSymbol());
        }
    }

    @Override
    public StockDTO getStockPrice(String symbol){

        String apiUrl = alphaVantageConfig.getBaseUrl() + "/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + alphaVantageConfig.getApiKey();

        ResponseEntity<AlphaVantageResponse> responseEntity = restTemplate.getForEntity(apiUrl, AlphaVantageResponse.class);
        AlphaVantageResponse response = responseEntity.getBody();

        if (stockRepository.findBySymbol(symbol).isPresent() && (response != null && response.getGlobalQuote().getSymbol() != null)) {
            StockDTO stockDTO = new StockDTO();
            stockDTO.setPrice(response.getGlobalQuote().getPrice());
            stockDTO.setSymbol(response.getGlobalQuote().getSymbol());
            stockDTO.setClose(response.getGlobalQuote().getClose());
            stockDTO.setHigh(response.getGlobalQuote().getHigh());
            stockDTO.setLow(response.getGlobalQuote().getLow());
            return stockDTO;
        } else {
            throw new RuntimeException("Unable to fetch stock price for symbol: " + symbol);
        }
    }

    @Override
    public List<Stock> getAllStocks(){
        return stockRepository.findAll();
    }

    @Override
    public String deleteById(Long id){
        if (stockRepository.existsById(id)) {
            stockRepository.deleteById(id);
            return "Successfully deleted Stock with id: " + id;
        }else {
            return "Stock not found with id: " + id;
        }
    }
}
