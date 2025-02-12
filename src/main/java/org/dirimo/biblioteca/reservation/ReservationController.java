package org.dirimo.biblioteca.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    // Get all reservations
    @GetMapping("/")
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    // Get reservation by ID
    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .orElseThrow(() -> new RuntimeException("Prenotazione con id " + id + " non trovata."));
    }

    // Add a new reservation
    @PostMapping("/")
    public Reservation createReservation(@RequestBody Reservation reservation) {
        return reservationService.saveReservation(reservation);
    }

    // Update a reservation
    @PutMapping("/{id}")
    public Reservation updateReservation(@PathVariable Long id, @RequestBody Reservation reservation) {
        return reservationService.updateReservation(id, reservation);
    }

    // Delete a reservation
    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }
}
