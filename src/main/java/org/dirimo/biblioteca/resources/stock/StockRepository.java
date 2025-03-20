package org.dirimo.biblioteca.resources.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.dirimo.biblioteca.resources.book.Book;


import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long>{
    Optional<Stock> findByBookId(Long id);
    Optional<Stock> findByBook(Book book);
}
