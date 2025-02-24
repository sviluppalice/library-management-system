package org.dirimo.biblioteca.resources.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dirimo.biblioteca.mail.MailProperties;
import org.dirimo.biblioteca.mail.MailService;
import org.dirimo.biblioteca.resources.reservation.event.CloseReservationEvent;
import org.dirimo.biblioteca.resources.reservation.event.OpenReservationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationListener {

    private final ReservationService reservationService;
    private final MailService mailService;

    @Async
    @EventListener
    public void onOpenReservation(OpenReservationEvent event){
        MailProperties mailProperties = reservationService.buildOpenReservationMailProperties(event.getReservation());
        mailService.sendMail(mailProperties);
    }

    @Async
    @EventListener
    public void onCloseReservation(CloseReservationEvent event){
        MailProperties mailProperties = reservationService.buildCloseReservationMailProperties(event.getReservation());
        mailService.sendMail(mailProperties);
    }
}