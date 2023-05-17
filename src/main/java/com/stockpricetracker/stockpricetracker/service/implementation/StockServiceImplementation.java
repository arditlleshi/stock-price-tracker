package com.stockpricetracker.stockpricetracker.service.implementation;

import com.stockpricetracker.stockpricetracker.config.AlphaVantageConfig;
import com.stockpricetracker.stockpricetracker.exceptions.ApiLimitExceededException;
import com.stockpricetracker.stockpricetracker.exceptions.StockAlreadyExistsException;
import com.stockpricetracker.stockpricetracker.exceptions.StockNotFoundException;
import com.stockpricetracker.stockpricetracker.exceptions.StockPriceFetchException;
import com.stockpricetracker.stockpricetracker.model.Stock;
import com.stockpricetracker.stockpricetracker.repository.StockRepository;
import com.stockpricetracker.stockpricetracker.service.ApiRequestService;
import com.stockpricetracker.stockpricetracker.service.StockService;
import com.stockpricetracker.stockpricetracker.util.AlphaVantageResponse;
import com.stockpricetracker.stockpricetracker.util.StockDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockServiceImplementation implements StockService {
    private final StockRepository stockRepository;
    private final RestTemplate restTemplate;
    private final AlphaVantageConfig alphaVantageConfig;
    private final ApiRequestService apiRequestService;

    @Override
    public Stock addStock(Stock stock){
        if (stockRepository.findBySymbol(stock.getSymbol()).isPresent()) {
            throw new StockAlreadyExistsException(stock.getSymbol());
        }
        String apiUrl = alphaVantageConfig.getBaseUrl() + "/query?function=GLOBAL_QUOTE&symbol=" + stock.getSymbol() + "&apikey=" + alphaVantageConfig.getApiKey();

        ResponseEntity<AlphaVantageResponse> responseEntity = restTemplate.getForEntity(apiUrl, AlphaVantageResponse.class);
        AlphaVantageResponse response = responseEntity.getBody();

        if (response != null && response.getGlobalQuote().getSymbol() != null) {
            stock.setSymbol(stock.getSymbol().toUpperCase());
            return stockRepository.save(stock);
        } else {
            throw new StockPriceFetchException(stock.getSymbol());
        }
    }

    @Override
    public StockDTO getStockPrice(String symbol){
        Optional<Stock> optionalStock = stockRepository.findBySymbol(symbol);
        if (optionalStock.isPresent()) {
            if (apiRequestService.isRequestLimitReached()) {
                throw new ApiLimitExceededException("API request limit exceeded (5 request per minute). Please try again later.");
            }
            if (apiRequestService.isDailyRequestLimitReached()) {
                throw new ApiLimitExceededException("Daily API request limit reached. Please try again tomorrow.");
            }

            apiRequestService.saveApiRequest();

            String apiUrl = alphaVantageConfig.getBaseUrl() + "/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + alphaVantageConfig.getApiKey();

            ResponseEntity<AlphaVantageResponse> responseEntity = restTemplate.getForEntity(apiUrl, AlphaVantageResponse.class);
            AlphaVantageResponse response = responseEntity.getBody();

            if (response != null && response.getGlobalQuote().getSymbol() != null) {
                StockDTO stockDTO = new StockDTO();
                stockDTO.setPrice(response.getGlobalQuote().getPrice());
                stockDTO.setSymbol(response.getGlobalQuote().getSymbol());
                stockDTO.setClose(response.getGlobalQuote().getClose());
                stockDTO.setHigh(response.getGlobalQuote().getHigh());
                stockDTO.setLow(response.getGlobalQuote().getLow());
                return stockDTO;
            } else {
                throw new StockPriceFetchException(symbol);
            }
        } else {
            throw new StockNotFoundException(symbol);
        }
    }

    @Override
    public List<Stock> getAllStocks(){
        return stockRepository.findAll();
    }

    @Override
    public Page<Stock> getAllStocks(Integer pageNumber, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return stockRepository.findAll(pageable);
    }

    @Override
    public String deleteById(Long id){
        if (stockRepository.existsById(id)) {
            stockRepository.deleteById(id);
            return "Successfully deleted Stock with id: " + id;
        } else {
            throw new StockNotFoundException(id);
        }
    }
}
