package org.dirimo.biblioteca.reservation;

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
import org.dirimo.biblioteca.reservation.enumerated.ReservationStatus;

import java.time.LocalDate;

@Entity
@Table(name="Reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resId;

    @Column(name="customer", nullable=false)
    private String customer;

    @Column(name="data_prenotazione", nullable=false)
    private LocalDate reservationDate;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private ReservationStatus status;

}
