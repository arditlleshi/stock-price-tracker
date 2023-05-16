package com.stockpricetracker.stockpricetracker.service.implementation;

import com.stockpricetracker.stockpricetracker.model.ApiRequest;
import com.stockpricetracker.stockpricetracker.repository.ApiRequestRepository;
import com.stockpricetracker.stockpricetracker.service.ApiRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ApiRequestServiceImplementation implements ApiRequestService {
    private final ApiRequestRepository apiRequestRepository;

    @Override
    public void saveApiRequest(){
        ApiRequest apiRequest = new ApiRequest(LocalDateTime.now());
        apiRequestRepository.save(apiRequest);
    }

    @Override
    public boolean isRequestLimitReached(){
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime startTime = currentTime.minusMinutes(1);
        long requestCount = apiRequestRepository.countByTimestampBetween(startTime, currentTime);
        return requestCount >= 5;
    }

    @Override
    public boolean isDailyRequestLimitReached(){
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime startTime = currentTime.withHour(0).withMinute(0).withSecond(0);
        long requestCount = apiRequestRepository.countByTimestampBetween(startTime, currentTime);
        return requestCount >= 500;
    }
}
