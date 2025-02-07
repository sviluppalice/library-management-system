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
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get a book by ID
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    // Add a new book
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    // Update a book
    public Book updateBook(Long id, Book bookDetails) {
        return bookRepository.findById(id).map(book -> {
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setCodISBN(bookDetails.getCodISBN());
            book.setYear(bookDetails.getYear());
            book.setGenre(bookDetails.getGenre());
            book.setPublisher(bookDetails.getPublisher());
            book.setLanguage(bookDetails.getLanguage());
            return bookRepository.save(book);
        }).orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
    }

    // Delete a book by ID
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}