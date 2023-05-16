package com.stockpricetracker.stockpricetracker.exceptions;

public class StockAlreadyExistsException extends RuntimeException {
    public StockAlreadyExistsException(String symbol){
        super("Stock with symbol " + symbol + " is already added on the database!");
    }
}
