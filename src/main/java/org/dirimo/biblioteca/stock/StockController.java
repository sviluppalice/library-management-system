package org.dirimo.biblioteca.stock;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("Stock")
public class StockController {

    private final StockService stockService;

    // Get all stocks
    @GetMapping("/")
    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    // Get stock by ID
    @GetMapping("/{id}")
    public Stock getStockById(@PathVariable Long id) {
        return stockService.getStockById(id)
                .orElseThrow(() -> new RuntimeException("Stock con id " + id + " non trovato."));
    }

    // Get stock by bookID
    @GetMapping("/book")
    public Optional<Stock> getStockByBookId(@RequestParam Long bookId) {
        return stockService.getStockByBookId(bookId);
    }

    // Create stock
    @PostMapping("/")
    public Stock createStock(@RequestBody Stock stock) {
        return stockService.saveStock(stock);
    }

    // Update a stock
    @PutMapping("/{id}")
    public Stock updateStock(@PathVariable Long id, @RequestBody Stock stock) {
        return stockService.updateStock(id, stock);
    }

    // Delete a shelf
    @DeleteMapping("/{id}")
    public void deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
    }
}