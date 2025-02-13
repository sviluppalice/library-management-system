package org.dirimo.biblioteca.stock;

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
    public List<Stock> getAll() {
        return stockRepository.findAll();
    }

    // Get a stock by ID
    public Optional<Stock> getById(Long id) {
        return stockRepository.findById(id);
    }

    // Get stock by book ID
    public Optional<Stock> getByBookId(Long bookId) {
        return stockRepository.findByBookBookId(bookId);
    }

    // Add a new stock
    public Stock create(Stock stock) {
        Long bookId = stock.getBook().getBookId();
        Book wholeBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Libro con Id " + bookId + " non trovato"));
        stock.setBook(wholeBook);
        return stockRepository.save(stock);
    }

    //Update a stock
    public Stock update(Long id, Stock stock) {
        stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock con ID: " + id));
        stock.setStockId(id);
        return stockRepository.save(stock);
    }

    // Delete a stock
    public void delete(Long id) {
        stockRepository.deleteById(id);
    }
}