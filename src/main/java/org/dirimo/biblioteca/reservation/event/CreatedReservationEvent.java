package org.dirimo.biblioteca.reservation.event;

import lombok.Getter;
import org.dirimo.biblioteca.reservation.Reservation;
import org.springframework.context.ApplicationEvent;

@Getter
public class CreatedReservationEvent extends ApplicationEvent {
    private final Reservation reservation;

    public CreatedReservationEvent(Object source, Reservation reservation) {
        super(source);
        this.reservation = reservation;
    }
}