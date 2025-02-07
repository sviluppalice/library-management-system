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
    public Stock updateStock(Long id, Stock stockDetails) {
        return stockRepository.findById(id).map(stock -> {
            stock.setBook(stockDetails.getBook());
            stock.setTotal_copies(stockDetails.getTotal_copies());
            stock.setAvailable_copies(stockDetails.getAvailable_copies());
            return stockRepository.save(stock);
        }).orElseThrow(() -> new RuntimeException("Stock not found with ID: " + id));
    }

    // Delete a stock
    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }
}