package com.stockpricetracker.stockpricetracker.service;

import com.stockpricetracker.stockpricetracker.model.Stock;
import com.stockpricetracker.stockpricetracker.util.StockDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StockService {
    Stock addStock(Stock stock);
    StockDTO getStockPrice(String symbol);
    List<Stock> getAllStocks();
    Page<Stock> getAllStocks(Integer pageNumber, Integer pageSize);
    String deleteById(Long id);
}
