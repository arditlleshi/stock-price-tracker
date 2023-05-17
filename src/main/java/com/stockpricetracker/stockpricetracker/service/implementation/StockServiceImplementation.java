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
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public Stock addStock(Stock stock){
        if (stockRepository.findBySymbol(stock.getSymbol().toUpperCase()).isPresent()) {
            throw new StockAlreadyExistsException(stock.getSymbol());
        }
        String apiUrl = alphaVantageConfig.getBaseUrl() + "/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=" + stock.getSymbol() + "&apikey=" + alphaVantageConfig.getApiKey();

        ResponseEntity<AlphaVantageResponse> responseEntity = restTemplate.getForEntity(apiUrl, AlphaVantageResponse.class);
        AlphaVantageResponse response = responseEntity.getBody();

        if (response != null && response.getTimeSeries() != null) {
            stock.setSymbol(stock.getSymbol().toUpperCase());
            return stockRepository.save(stock);
        } else {
            throw new StockPriceFetchException(stock.getSymbol());
        }
    }

    @Override
    @Transactional
    public StockDTO getStockPrice(String symbol){
        Optional<Stock> optionalStock = stockRepository.findBySymbol(symbol.toUpperCase());
        if (optionalStock.isPresent()) {
            if (apiRequestService.isRequestLimitReached()) {
                throw new ApiLimitExceededException("API request limit exceeded (5 calls per minute). Please try again later.");
            }
            if (apiRequestService.isDailyRequestLimitReached()) {
                throw new ApiLimitExceededException("Daily API request limit reached (500 calls per day). Please try again tomorrow.");
            }

            apiRequestService.saveApiRequest();

            String apiUrl = alphaVantageConfig.getBaseUrl() + "/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=" + symbol + "&apikey=" + alphaVantageConfig.getApiKey();

            ResponseEntity<AlphaVantageResponse> responseEntity = restTemplate.getForEntity(apiUrl, AlphaVantageResponse.class);
            AlphaVantageResponse response = responseEntity.getBody();

            if (response != null && response.getMetaData() != null && response.getTimeSeries() != null) {
                StockDTO stockDTO = new StockDTO();
                stockDTO.setInformation(response.getMetaData().getInformation());
                stockDTO.setSymbol(response.getMetaData().getSymbol());
                stockDTO.setLastRefreshed(response.getMetaData().getLastRefreshed());
                stockDTO.setOutputSize(response.getMetaData().getOutputSize());
                stockDTO.setTimeZone(response.getMetaData().getTimeZone());
                stockDTO.setTimeSeries(response.getTimeSeries());
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
    @Transactional
    public String deleteById(Long id){
        if (stockRepository.existsById(id)) {
            stockRepository.deleteById(id);
            return "Successfully deleted Stock with id: " + id;
        } else {
            throw new StockNotFoundException(id);
        }
    }
}
