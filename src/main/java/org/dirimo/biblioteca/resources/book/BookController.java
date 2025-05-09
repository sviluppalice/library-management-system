package org.dirimo.biblioteca.resources.book;

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

@Slf4j
@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("Book")
public class BookController {

    private final BookService bookService;

    // Get all books
    @GetMapping("/")
    public List<Book> getAll() {
        return bookService.getAll();
    }

    // Get book by ID
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .orElseThrow(() -> new RuntimeException("Libro con id " + id + " non trovato."));
    }

    // Get books by shelf ID
    @GetMapping("/shelf")
    public List<Book> getBooksByShelfId(@RequestParam Long shelfId) {
        return bookService.getBooksByShelfId(shelfId);
    }

    // Add a new book
    @PostMapping("/")
    public Book create(@RequestBody Book book) {
        return bookService.create(book);
    }

    // Add in bulk
    @PostMapping("/bulk")
    public List<Book> createBulk(@RequestBody List<Book> books) {
        return bookService.createBulk(books);
    }

    // Update a book
    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @RequestBody Book book) {
        return bookService.update(id, book);
    }

    // Delete a book
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }

    // TESTING - Endpoint manuale per testare lo scheduler con Postman
    @PostMapping("/trigger")
    public String triggerUpdateCatalog() {
        bookService.sendCatalog();
        return "Catalog sent manually.";
    }
}
