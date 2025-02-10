package org.dirimo.biblioteca.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    // Get all reservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Get a reservation by ID
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    // Add a new reservation
    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    // Update a reservation
    public Reservation updateReservation(Long id, Reservation reservation) {
        reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prenotazione con id: " + id + " non trovata"));
        return reservationRepository.save(reservation);
    }

    // Delete a reservation by ID
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}