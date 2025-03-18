package org.dirimo.biblioteca.resources.book;

import lombok.RequiredArgsConstructor;
import org.dirimo.biblioteca.resources.stock.Stock;
import org.dirimo.biblioteca.resources.stock.StockService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final StockService stockService;

    //Singleton: una sola istanza di una classe in tutto il sistema

    // Get all books
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    // Get a book by ID
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    // Get books by shelf ID
    public List<Book> getBooksByShelfId(Long shelfId) {
        return bookRepository.findBooksByShelfShelfId(shelfId);
    }

    // Add a new book
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    // Add in bulk
    public List<Book> createBulk(List<Book> books) {
        List<Book> savedBooks = bookRepository.saveAll(books);

        for (Book book : savedBooks) {
            Stock stock = new Stock();
            stock.setBook(book);
            stock.setTotalCopies(50);
            stock.setAvailableCopies(50);

            stockService.create(stock);
        }

        return savedBooks;
    }

    // Update a book
    public Book update(Long id, Book book) {
        bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Libro con ID: " + id + " non trovato."));
        return bookRepository.save(book);
    }

    // Delete a book by ID
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}