package com.mandacarubroker.controller;


import com.mandacarubroker.domain.stock.RequestStockDTO;
import com.mandacarubroker.domain.stock.Stock;
import com.mandacarubroker.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    @GetMapping("/{id}")
    public Stock getStockById(@PathVariable String id) {
        Stock stock = stockService.getStockById(id).orElse(null);
        if(stock == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock not found");
        }
        return stock;
    }

    @PostMapping
    public ResponseEntity<Stock> createStock(@RequestBody RequestStockDTO data) {
        Stock createdStock = stockService.createStock(data);
        URI location = UriComponentsBuilder.fromPath("/stocks/{id}")
                .buildAndExpand(createdStock.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdStock);
    }

    @PutMapping("/{id}")
    public Stock updateStock(@PathVariable String id, @RequestBody RequestStockDTO data) {
        return stockService.updateStock(id, data).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteStock(@PathVariable String id) {
        stockService.deleteStock(id);
    }

}
