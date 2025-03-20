package org.dirimo.biblioteca.resources.book;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dirimo.biblioteca.common.BaseEntity;
import org.dirimo.biblioteca.resources.shelf.Shelf;

@Entity
@Table(name="Books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shelf_id")
    private Shelf shelf;

    @Column(name = "isbn", nullable = false)
    private String isbn;

    @Column(name = "titolo", nullable = false)
    private String title;

    @Column(name = "autore", nullable = false)
    private String author;

    @Column(name = "anno", nullable = false, length = 4)
    private Integer year;

    @Column(name = "genere", nullable = false)
    private String genre;

    @Column(name = "casa_editrice", nullable = false)
    private String publisher;

    @Column(name = "lingua", nullable = false)
    private String language;

    @Column(name = "descrizione", nullable = false)
    private String description;

}
