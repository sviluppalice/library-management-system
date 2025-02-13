package org.dirimo.biblioteca.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.dirimo.biblioteca.book.Book;


import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long>{
    Optional<Stock> findByBookBookId(Long bookId);
    Optional<Stock> findByBook(Book book);
}
