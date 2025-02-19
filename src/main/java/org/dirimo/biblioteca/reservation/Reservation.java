package org.dirimo.biblioteca.reservation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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

    @Column(name="cliente", nullable=false)
    private String customer;

    @Column(name="email_cliente")
    private String email;

    @Column(name="data_start", nullable=false)
    private LocalDate resStartDate;

    @Column(name="data_end")
    private LocalDate resEndDate;

    @Column(name="data_scadenza")
    private LocalDate resExpiryDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "status")
    private ReservationStatus status;

    @PrePersist
    private void onCreate() {
        resStartDate = LocalDate.now();
        resExpiryDate = LocalDate.now().plusDays(15);
        status = ReservationStatus.ACTIVE;
    }
}
