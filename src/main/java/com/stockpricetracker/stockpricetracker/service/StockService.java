package com.stockpricetracker.stockpricetracker.service;

import com.stockpricetracker.stockpricetracker.model.Stock;
import com.stockpricetracker.stockpricetracker.util.StockDTO;

import java.util.List;

public interface StockService {
    Stock addStock(Stock stock);
    StockDTO getStockPrice(String symbol);
    List<Stock> getAllStocks();
    String deleteById(Long id);
}
