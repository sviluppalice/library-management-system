package org.dirimo.biblioteca.stock;

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
import org.dirimo.biblioteca.book.Book;

@Entity
@Table(name="Stocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    @ManyToOne
    @JoinColumn(name = "book_id", unique = true, nullable = false)
    private Book book;

    @Column(name="totale_copie", columnDefinition = "int default 1", nullable = false)
    private int totalCopies;

    @Column(name="copie_disponibili", nullable = false)
    private int availableCopies;

    public void handleQuantity(int quantity) {
        availableCopies += quantity;

    }
}