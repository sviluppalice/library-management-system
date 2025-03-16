package org.dirimo.biblioteca.resources.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dirimo.biblioteca.resources.book.dto.BookDTO;
import org.dirimo.biblioteca.resources.customer.dto.CustomerDTO;
import org.dirimo.biblioteca.resources.reservation.Reservation;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private Long reservationId;
    private BookDTO book;
    private CustomerDTO customer;
    private LocalDate resStartDate;
    private LocalDate resEndDate;

    public static ReservationDTO fromReservation(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        BookDTO bookDTO = null;
        if (reservation.getBook() != null) {
            bookDTO = new BookDTO(
                    reservation.getBook().getBookId(),
                    reservation.getBook().getCodISBN(),
                    reservation.getBook().getTitle(),
                    reservation.getBook().getAuthor(),
                    reservation.getBook().getYear(),
                    reservation.getBook().getGenre(),
                    reservation.getBook().getPublisher(),
                    reservation.getBook().getLanguage(),
                    reservation.getBook().getDescription()
            );
        }

        CustomerDTO customerDTO = null;
        if (reservation.getCustomer() != null) {
            customerDTO = new CustomerDTO(
                    reservation.getCustomer().getId(),
                    reservation.getCustomer().getFirstName(),
                    reservation.getCustomer().getLastName(),
                    reservation.getCustomer().getEmail()
            );
        }

        return new ReservationDTO(
                reservation.getResId(),
                bookDTO,
                customerDTO,
                reservation.getResStartDate(),
                reservation.getResEndDate()
        );
    }
}