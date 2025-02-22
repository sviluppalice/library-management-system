package org.dirimo.biblioteca.resources.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
    List<Book> findBooksByShelfShelfId(Long shelfId);
}
