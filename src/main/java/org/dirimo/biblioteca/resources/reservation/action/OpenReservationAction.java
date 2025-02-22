package org.dirimo.biblioteca.resources.reservation.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dirimo.biblioteca.resources.reservation.Reservation;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpenReservationAction {
    private Reservation reservation;
    private LocalDate date;
}