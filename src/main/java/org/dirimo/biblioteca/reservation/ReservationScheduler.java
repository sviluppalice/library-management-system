package org.dirimo.biblioteca.reservation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dirimo.biblioteca.reservation.enumerated.ReservationStatus;
import org.dirimo.biblioteca.reservation.event.CreatedReservationEvent;
import org.dirimo.biblioteca.reservation.mail.MailProperties;
import org.dirimo.biblioteca.reservation.mail.MailerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@EnableAsync
@AllArgsConstructor
@Component
public class ReservationScheduler {

    private final ReservationService reservationService;
    private static final Logger logger = LoggerFactory.getLogger(ReservationScheduler.class);
    private final MailerService mailerService;

    @Async
    @Scheduled(cron = "0 0 9 * * *")//"0 * * * * *" ogni minuto - SOLO TESTING
    public void expiringReservationReminder() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(3);
        ReservationStatus status = ReservationStatus.ACTIVE;

        List<Reservation> expiringReservations = reservationService.getExpiring(status, startDate, endDate);

        for (Reservation r : expiringReservations) {
            MailProperties mailProperties = reservationService.buildExpiringReminderMailProperties(r);
            mailerService.sendMail(mailProperties);
            logger.info("Expiring Reservation Reminder sent to: " + r.getEmail());
        }
    }

    @Async
    @Scheduled(cron = "0 0 9 * * *")//"0 * * * * *" ogni minuto - SOLO TESTING
    public void expiredReservationNotice(){
        LocalDate today = LocalDate.now();
        ReservationStatus status = ReservationStatus.ACTIVE;

        List<Reservation> expiredReservations = reservationService.getExpired(status, today);
        for (Reservation r : expiredReservations) {
            MailProperties mailProperties = reservationService.buildExpiredNoticeMailProperties(r);
            mailerService.sendMail(mailProperties);
            logger.info("Expired Reservation Notice sent to: " + r.getEmail());
        }
    }

    @Async
    @EventListener
    public void handleCreatedReservation(CreatedReservationEvent event){
        Reservation r = event.getReservation();
        MailProperties mailProperties = reservationService.buildCreatedReservationMailProperties(r);
        mailerService.sendMail(mailProperties);
        logger.info("Created Reservation Email sent to: " + r.getEmail());
    }
}