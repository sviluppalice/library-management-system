package org.dirimo.biblioteca.resources.stock;

import lombok.RequiredArgsConstructor;
import org.dirimo.biblioteca.resources.book.Book;
import org.dirimo.biblioteca.resources.book.BookRepository;
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
        return stockRepository.findByBookId(bookId);
    }

    // Get stock by book
    public Optional<Stock> getByBook(Book book) {
        return stockRepository.findByBook(book);
    }

    // Add a new stock
    public Stock create(Stock stock) {
        Long id = stock.getBook().getId();
        Book wholeBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro con Id " + id + " non trovato"));
        stock.setBook(wholeBook);
        return stockRepository.save(stock);
    }

    //Update a stock
    public Stock update(Long id, Stock stock) {
        stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock con ID: " + id));
        return stockRepository.save(stock);
    }

    // Delete a stock
    public void delete(Long id) {
        stockRepository.deleteById(id);
    }
}