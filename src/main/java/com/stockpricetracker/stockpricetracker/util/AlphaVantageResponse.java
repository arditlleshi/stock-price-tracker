package com.stockpricetracker.stockpricetracker.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlphaVantageResponse {
    @JsonProperty("Global Quote")
    private GlobalQuote globalQuote;

    public GlobalQuote getGlobalQuote() {
        return globalQuote;
    }

    public void setGlobalQuote(GlobalQuote globalQuote) {
        this.globalQuote = globalQuote;
    }

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

        public String getSymbol(){
            return symbol;
        }

        public void setSymbol(String symbol){
            this.symbol = symbol;
        }

        public double getHigh(){
            return high;
        }

        public void setHigh(double high){
            this.high = high;
        }

        public double getLow(){
            return low;
        }

        public void setLow(double low){
            this.low = low;
        }

        public double getPrice(){
            return price;
        }

        public void setPrice(double price){
            this.price = price;
        }

        public double getClose(){
            return close;
        }

        public void setClose(double close){
            this.close = close;
        }
    }
}
