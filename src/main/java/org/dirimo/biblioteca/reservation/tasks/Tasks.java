package org.dirimo.biblioteca.reservation.tasks;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dirimo.biblioteca.reservation.Reservation;
import org.dirimo.biblioteca.reservation.ReservationService;
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
    @Scheduled(cron = "0 0/1 * * * *") //"0 0 9 * * *"
    public void expiringReservationReminder() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(3);

        List<Reservation> expiringReservations = reservationService.getExpiringReservations(startDate, endDate);

        for (Reservation r : expiringReservations) {
            String email = r.getEmail();
            String subject = "Reservation expiration notice | "+ r.getResExpiryDate();
            String content = mailerService.buildEmailContent(r);
            mailerService.sendMail(email, subject, content);
            logger.info("Email sent to: " + email + " Subject: " + subject);
        }

    }
}
