package com.stockpricetracker.stockpricetracker.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockDTO{
    private String symbol;
    private double price;
    private double close;
    private double high;
    private double low;
}
