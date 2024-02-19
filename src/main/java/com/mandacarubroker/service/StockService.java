package com.mandacarubroker.service;

import com.mandacarubroker.domain.stock.RequestStockDTO;
import com.mandacarubroker.domain.stock.Stock;
import com.mandacarubroker.domain.stock.StockRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StockService {


    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Optional<Stock> getStockById(String id) {
        return stockRepository.findById(id);
    }

    public Stock createStock(RequestStockDTO data) {
        Stock newStock = new Stock(data);
        validateRequestStockDTO(data);
        return stockRepository.save(newStock);
    }

    public Optional<Stock> updateStock(String id, RequestStockDTO data) {
        Optional<Stock> optionalStock = stockRepository.findById(id);

        if(optionalStock.isPresent()) {
            Stock stock = optionalStock.get();
            validateRequestStockDTO(data);
            Stock updatedStock = new Stock(data);
            stock.setSymbol(updatedStock.getSymbol());
            stock.setCompanyName(updatedStock.getCompanyName());
            double newPrice = stock.changePrice(updatedStock.getPrice());
            stock.setPrice(newPrice);

            return Optional.of(stockRepository.save(stock));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock not found with id: " + id);
        }
    }

    public void deleteStock(String id) {
        Optional<Stock> optionalStock = stockRepository.findById(id);

        if(optionalStock.isPresent()) {
            stockRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock not found with id: " + id);
        }
    }

    public static void validateRequestStockDTO(RequestStockDTO data) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<RequestStockDTO>> violations = validator.validate(data);

        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed. Details: ");

            for (ConstraintViolation<RequestStockDTO> violation : violations) {
                errorMessage.append(String.format("[%s: %s], ", violation.getPropertyPath(), violation.getMessage()));
            }

            errorMessage.delete(errorMessage.length() - 2, errorMessage.length());

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage.toString());
        }

    }
}
