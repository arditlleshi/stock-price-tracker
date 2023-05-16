package com.stockpricetracker.stockpricetracker.exceptions;

public class StockNotFoundException extends RuntimeException{
    public StockNotFoundException(String symbol){
        super("Stock not found for symbol: " + symbol);
    }

    public StockNotFoundException(Long id){
        super("Stock not found with id: " + id);
    }
}
