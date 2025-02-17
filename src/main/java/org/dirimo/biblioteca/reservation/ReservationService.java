package org.dirimo.biblioteca.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dirimo.biblioteca.book.Book;
import org.dirimo.biblioteca.reservation.enumerated.ReservationStatus;
import org.dirimo.biblioteca.stock.Stock;
import org.dirimo.biblioteca.stock.StockService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StockService stockService;

    // Get all reservations
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    // Get a reservation by ID
    public Optional<Reservation> getById(Long id) {
        return reservationRepository.findById(id);
    }

    // Add a new reservation
    /*    public Reservation create(Reservation reservation) {
     *      return reservationRepository.save(reservation);
     *    }
     */

    // Update a reservation
    public Reservation update(Long id, Reservation reservation) {
        reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prenotazione con id: " + id + " non trovata"));
        return reservationRepository.save(reservation);
    }

    // Delete a reservation by ID
    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

    // Close a reservation
    public Reservation close(Long id, LocalDate date) {
        // Updates reservation
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prenotazione con id: " + id + " non trovata"));
        reservation.setStatus(ReservationStatus.CLOSED);
        reservation.setResEndDate(date);

        // Updates stock
        Book book = reservation.getBook();
        Optional<Stock> stockOptional = stockService.getByBook(book);
        Stock stock = stockOptional.orElseThrow(() ->
                new RuntimeException("Libro con id: " + book.getBookId() + " non trovato.")
        );
        stock.handleQuantity(1);

        int diff = (int) ChronoUnit.DAYS.between(reservation.getResStartDate(), reservation.getResEndDate());
        log.info("Hai riportato indietro il libro in " + diff + " giorni.");
        return reservation;
    }

    public Reservation open(Reservation reservation, LocalDate date) {
        // Check stock existence
        Book book = reservation.getBook();
        Optional<Stock> stockOptional = stockService.getByBook(book);
        Stock stock = stockOptional.orElseThrow(() ->
                new RuntimeException("Libro con id: " + book.getBookId() + " non trovato.")
        );

        // Check copies availability and if available, creates reservation
        if (stock.getAvailableCopies() <= 0) {
            throw new RuntimeException("Non ci sono copie del libro " + book.getBookId() + " disponibili al momento.");
        } else {
            reservation.setStatus(ReservationStatus.ACTIVE);
            reservation.setResStartDate(date);
            reservation.setResExpiryDate(date.plusDays(15));
            stock.handleQuantity(-1);
            return reservationRepository.save(reservation);
        }
    }

    public List<Reservation> getExpiringReservations(LocalDate startDate, LocalDate endDate) {
        return reservationRepository.findExpiringReservations(startDate, endDate);
    }
}