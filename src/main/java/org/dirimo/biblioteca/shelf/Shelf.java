package org.dirimo.biblioteca.shelf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dirimo.biblioteca.book.Book;
import org.dirimo.biblioteca.zone.Zone;

import java.util.List;

@Entity
@Table(name="Shelves")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Shelf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "shelf")
    @JsonIgnore
    private List<Book> books;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;
}