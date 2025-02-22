package org.dirimo.biblioteca.resources.reservation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dirimo.biblioteca.resources.reservation.action.CloseReservationAction;
import org.dirimo.biblioteca.resources.reservation.action.OpenReservationAction;
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
@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("Reservation")
public class ReservationController {

    private final ReservationService reservationService;

    // Get all reservations
    @GetMapping("/")
    public List<Reservation> getAll() {
        return reservationService.getAll();
    }

    // Get reservation by ID
    @GetMapping("/{id}")
    public Reservation getById(@PathVariable Long id) {
        return reservationService.getById(id)
                .orElseThrow(() -> new RuntimeException("Prenotazione con id " + id + " non trovata."));
    }

    // Get reservation by Customer ID

    // Add a new reservation
    @PostMapping("/")
    public Reservation create(@RequestBody Reservation reservation) {
        return reservationService.create(reservation);
    }

    // Add a new reservation
    @PostMapping("/open/")
    public Reservation open(@RequestBody OpenReservationAction action) {
       return reservationService.open(action.getReservation(), action.getDate());
    }

    // Update a reservation
    @PutMapping("/{id}")
    public Reservation update(@PathVariable Long id, @RequestBody Reservation reservation) {
        return reservationService.update(id, reservation);
    }

    // Delete a reservation
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reservationService.delete(id);
    }

    @PostMapping("/close/{id}")
    public Reservation close(@PathVariable Long id, @RequestBody CloseReservationAction action) {
        return reservationService.close(id, action.getDate());
    }
}
