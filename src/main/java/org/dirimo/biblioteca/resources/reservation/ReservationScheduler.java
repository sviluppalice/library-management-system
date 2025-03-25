package org.dirimo.biblioteca.resources.reservation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dirimo.biblioteca.resources.reservation.enumerated.ReservationStatus;
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

    @Scheduled(cron = "0 0 9 * * *")//"0 * * * * *" ogni minuto - SOLO TESTING
    public void expiringReservationReminder() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(3);
        ReservationStatus status = ReservationStatus.ACTIVE;

        List<Reservation> expiringReservations = reservationService.getExpiring(status, startDate, endDate);
        for (Reservation r : expiringReservations) {
            reservationService.sendExpiringReminderMail(r);
        }
    }

    @Scheduled(cron = "0 0 9 * * *")//"0 * * * * *" ogni minuto - SOLO TESTING
    public void expiredReservationNotice(){
        LocalDate today = LocalDate.now();
        ReservationStatus status = ReservationStatus.ACTIVE;

        List<Reservation> expiredReservations = reservationService.getExpired(status, today);
        for (Reservation r : expiredReservations) {
            reservationService.sendExpiredNoticeMail(r);
        }
    }
}