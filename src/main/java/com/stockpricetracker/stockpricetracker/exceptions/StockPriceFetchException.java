package com.stockpricetracker.stockpricetracker.exceptions;

public class StockPriceFetchException extends RuntimeException {
    public StockPriceFetchException(String symbol){
        super("Failed to fetch stock price for symbol: " + symbol);
    }
}
