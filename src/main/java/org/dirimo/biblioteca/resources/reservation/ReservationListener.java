package org.dirimo.biblioteca.resources.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dirimo.biblioteca.mail.MailProperties;
import org.dirimo.biblioteca.mail.MailService;
import org.dirimo.biblioteca.resources.reservation.event.CloseReservationEvent;
import org.dirimo.biblioteca.resources.reservation.event.OpenReservationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationListener {

    private final ReservationService reservationService;
    private final MailService mailService;
    private static final Logger logger = LoggerFactory.getLogger(ReservationListener.class);

    @Async
    @EventListener
    public void handleCreatedReservation(OpenReservationEvent event){
        MailProperties mailProperties = reservationService.buildCreatedReservationMailProperties(event.getReservation());
        mailService.sendMail(mailProperties);
        logger.info("Created Reservation Email sent to: " + event.getReservation().getCustomer().getEmail());
    }

    @Async
    @EventListener
    public void handleClosedReservation(CloseReservationEvent event){
        MailProperties mailProperties = reservationService.buildClosedReservationMailProperties(event.getReservation());
        mailService.sendMail(mailProperties);
        logger.info("Closed Reservation Email sent to: " + event.getReservation().getCustomer().getEmail());
    }
}