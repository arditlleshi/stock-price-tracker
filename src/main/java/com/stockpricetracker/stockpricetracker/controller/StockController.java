package com.stockpricetracker.stockpricetracker.controller;

import com.stockpricetracker.stockpricetracker.model.Stock;
import com.stockpricetracker.stockpricetracker.service.StockService;
import com.stockpricetracker.stockpricetracker.util.StockDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stocks")
public class StockController {
    private final StockService stockService;

    @PostMapping()
    public ResponseEntity<Stock> addStock(@RequestBody Stock stock){
        return ResponseEntity.ok(stockService.addStock(stock));
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<StockDTO> getStockBySymbol(@PathVariable String symbol){
        return ResponseEntity.ok(stockService.getStockPrice(symbol));
    }

    @GetMapping()
    public ResponseEntity<List<Stock>> getAllStocks(){
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<Stock>> getAllStocks(@PathVariable Integer pageNumber, @PathVariable Integer pageSize){
        return ResponseEntity.ok(stockService.getAllStocks(pageNumber, pageSize));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStockById(@PathVariable Long id){
        return ResponseEntity.ok(stockService.deleteById(id));
    }
}
