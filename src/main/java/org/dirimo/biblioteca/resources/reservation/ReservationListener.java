package org.dirimo.biblioteca.resources.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dirimo.commonlibrary.event.GenericModuleEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationListener {

    private final ReservationService reservationService;

    @EventListener
    public void handleReservationEvent(GenericModuleEvent<Reservation> event) {
        switch (event.getEventType()) {
            case OPENED -> {
                reservationService.sendOpenReservationJMS(event.getPayload());
                //reservationService.sendOpenReservationMail(event.getPayload());
            }

            case CLOSED -> {
                //reservationService.sendCloseReservationMail(event.getPayload());
            }
        }
    }
}