package org.dirimo.biblioteca.stock;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.dirimo.biblioteca.book.Book;
import org.dirimo.biblioteca.book.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor()
public class StockService {

    private final StockRepository stockRepository;
    private final BookRepository bookRepository;

    // Get all stocks
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    // Get a stock by ID
    public Optional<Stock> getStockById(Long id) {
        return stockRepository.findById(id);
    }

    // Get stock by book ID
    public Optional<Stock> findByBookId(Long bookId) {
        return stockRepository.findByBookId(bookId);
    }

    // Add a new stock
    @Transactional
    public Stock saveStock(Stock stock) {
        Long bookId = stock.getBook().getId();
        Book wholeBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Libro con Id " + bookId + " non trovato"));

        stock.setBook(wholeBook);

        return stockRepository.save(stock);
    }

    //Update a stock
    @Transactional
    public Stock updateStock(Long id, Stock stock) {
        stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock con ID: " + id));
        stock.setId(id);
        return stockRepository.save(stock);
    }

    // Delete a stock
    @Transactional
    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }
}