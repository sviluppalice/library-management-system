package org.dirimo.biblioteca.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

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

    // Update a book
    public Book update(Long id, Book book) {
        bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Libro con ID: " + id + " non trovato."));
        book.setBookId(id);
        return bookRepository.save(book);
    }

    // Delete a book by ID
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}