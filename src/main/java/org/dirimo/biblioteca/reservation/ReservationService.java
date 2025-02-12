package org.dirimo.biblioteca.reservation;

import lombok.RequiredArgsConstructor;
import org.dirimo.biblioteca.stock.Stock;
import org.dirimo.biblioteca.stock.StockRepository;
import org.dirimo.biblioteca.stock.StockService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StockService stockService;
    private final StockRepository stockRepository;

    // Get all reservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Get a reservation by ID
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    // Add a new reservation
    public Reservation saveReservation(Reservation reservation) throws RuntimeException {
        Long bookId = reservation.getBook().getId(); // prendo id del libro da prenotazione
        Optional<Stock> stockOptional = stockService.findByBookId(bookId); // metto in optional perché potrebbe non esistere

        Stock stock = stockOptional.orElseThrow(() ->
                new RuntimeException("Libro con id: " + bookId + " non trovato.")
        ); // controllo esistenza mettendo in oggetto stock

        if (stock.getAvailable_copies() <= 0) {
            throw new RuntimeException("Non ci sono copie del libro " + bookId + " disponibili al momento.");
        } else { //controllo che copie disponibili > 0
            stock.setAvailable_copies(stock.getAvailable_copies() - 1); // tolgo 1 alle copie disponibili del libro
            return reservationRepository.save(reservation); // salvo prenotazione e restituisco
        }
    }

    // Update a reservation
    public Reservation updateReservation(Long id, Reservation reservation) {
        reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prenotazione con id: " + id + " non trovata"));
        reservation.setId(id);
        return reservationRepository.save(reservation);
    }

    // Delete a reservation by ID
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}