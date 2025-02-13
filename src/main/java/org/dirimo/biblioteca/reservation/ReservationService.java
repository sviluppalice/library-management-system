package org.dirimo.biblioteca.reservation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.dirimo.biblioteca.stock.Stock;
import org.dirimo.biblioteca.stock.StockService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StockService stockService;

    // Get all reservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Get a reservation by ID
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    // Add a new reservation
    @Transactional
    public Reservation saveReservation(Reservation reservation) {
        Long bookId = reservation.getBook().getId();
        Optional<Stock> stockOptional = stockService.findByBookId(bookId);

        Stock stock = stockOptional.orElseThrow(() ->
                new RuntimeException("Libro con id: " + bookId + " non trovato.")
        );

        if (stock.getAvailable_copies() <= 0) {
            throw new RuntimeException("Non ci sono copie del libro " + bookId + " disponibili al momento.");
        } else {
            stock.setAvailable_copies(stock.getAvailable_copies() - 1);
            return reservationRepository.save(reservation);
        }
    }

    // Update a reservation
    @Transactional
    public Reservation updateReservation(Long id, Reservation reservation) {
        reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prenotazione con id: " + id + " non trovata"));
        reservation.setId(id);
        return reservationRepository.save(reservation);
    }

    // Delete a reservation by ID
    @Transactional
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}