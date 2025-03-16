package org.dirimo.biblioteca.resources.book.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long bookId;
    private String isbn;
    private String title;
    private String author;
    private Integer year;
    private String genre;
    private String publisher;
    private String language;
    private String description;
}