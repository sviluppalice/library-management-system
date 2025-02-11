package org.dirimo.biblioteca.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    // Get all stocks
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    // Get a stock by ID
    public Optional<Stock> getStockById(Long id) {
        return stockRepository.findById(id);
    }

    // Add a new stock
    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }

    //Update a stock
    public Stock updateStock(Long id, Stock stock) {
        stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock con ID: " + id));
        return stockRepository.save(stock);
    }

    // Delete a stock
    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }
}