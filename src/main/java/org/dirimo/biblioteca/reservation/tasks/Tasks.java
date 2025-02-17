package org.dirimo.biblioteca.reservation.tasks;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dirimo.biblioteca.reservation.Reservation;
import org.dirimo.biblioteca.reservation.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@EnableAsync
@AllArgsConstructor
@Component
public class Tasks {

    private final ReservationService reservationService;
    private static final Logger logger = LoggerFactory.getLogger(Tasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    @Async
    @Scheduled(cron = "0 * * * * *")
    public void expiringReservationReminder() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(3);

        List<Reservation> expiringReservations = reservationService.getExpiringReservations(startDate, endDate);

        for (Reservation r : expiringReservations) {
            logger.info("Hi " + r.getCustomer() + ", " +
                    "Your reservation is going to expire on " + r.getResExpiryDate() + ". " +
                    "Please return your book or delegate another person to return your book before the " +
                    "expiry date to avoid morosity fees.");
        }

    }
}
