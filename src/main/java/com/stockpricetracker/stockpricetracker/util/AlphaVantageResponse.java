package com.stockpricetracker.stockpricetracker.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlphaVantageResponse {
    @JsonProperty("Global Quote")
    private GlobalQuote globalQuote;

    @Getter
    @Setter
    public static class GlobalQuote {
        @JsonProperty("01. symbol")
        private String symbol;
        @JsonProperty("03. high")
        private double high;
        @JsonProperty("04. low")
        private double low;
        @JsonProperty("05. price")
        private double price;
        @JsonProperty("08. previous close")
        private double close;
    }
}
