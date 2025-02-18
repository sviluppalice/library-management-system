package org.dirimo.biblioteca.reservation.tasks;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dirimo.biblioteca.reservation.Reservation;
import org.dirimo.biblioteca.reservation.ReservationService;
import org.dirimo.biblioteca.reservation.enumerated.ReservationStatus;
import org.dirimo.biblioteca.reservation.mail.MailerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class Tasks {

    private final ReservationService reservationService;
    private static final Logger logger = LoggerFactory.getLogger(Tasks.class);
    private final MailerService mailerService;

    @Async
    @Scheduled(cron = "0 0 9 * * *")//"0 * * * * *" ogni minuto - SOLO TESTING
    public void expiringReservationReminder() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(3);
        ReservationStatus status = ReservationStatus.ACTIVE;

        List<Reservation> expiringReservations = reservationService.getExpiring(status, startDate, endDate);

        for (Reservation r : expiringReservations) {
            String email = r.getEmail();
            String subject = "Reminder: Your Library Reservation Expires Soon! "+ r.getResExpiryDate();
            String content = mailerService.buildExpiringReminderContent(r);
            mailerService.sendMail(email, subject, content);
            logger.info("Expiring Reservation Reminder sent to: " + email + " Subject: " + subject);
        }
    }

    @Async
    @Scheduled(cron = "0 0 9 * * *")//"0 * * * * *" ogni minuto - SOLO TESTING
    public void expiredReservationNotice(){
        LocalDate today = LocalDate.now();
        ReservationStatus status = ReservationStatus.ACTIVE;

        List<Reservation> expiredReservations = reservationService.getExpired(status, today);
        for (Reservation r : expiredReservations) {
            String email = r.getEmail();
            String subject = "Notice: Your Library Reservation Expired on "+ r.getResExpiryDate();
            String content = mailerService.buildExpirationNoticeContent(r);
            mailerService.sendMail(email, subject, content);
            logger.info("Expired Reservation Notice sent to: " + email + " Subject: " + subject);
        }
    }
}
