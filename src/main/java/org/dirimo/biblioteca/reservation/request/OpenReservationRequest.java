package org.dirimo.biblioteca.reservation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dirimo.biblioteca.reservation.Reservation;
import org.dirimo.biblioteca.reservation.action.OpenReservationAction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpenReservationRequest {
    private Reservation reservation;
    private OpenReservationAction action;
}
