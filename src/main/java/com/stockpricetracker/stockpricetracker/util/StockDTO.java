package com.stockpricetracker.stockpricetracker.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockDTO{
    private String information;
    private String symbol;
    private LocalDate lastRefreshed;
    private String outputSize;
    private String timeZone;
    private Map<String, StockData> timeSeries;
}
