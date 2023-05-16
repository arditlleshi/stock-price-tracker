package com.stockpricetracker.stockpricetracker.service;

public interface ApiRequestService {
    void saveApiRequest();
    boolean isRequestLimitReached();
    boolean isDailyRequestLimitReached();
}
