package org.dirimo.biblioteca.resources.reservation;

import org.dirimo.biblioteca.resources.reservation.enumerated.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{
    @Query("SELECT r FROM Reservation r WHERE r.resExpiryDate BETWEEN :startDate AND :endDate AND r.status = :status")
    public List<Reservation> findExpiring(ReservationStatus status, LocalDate startDate, LocalDate endDate);

    @Query("SELECT r FROM Reservation r WHERE r.resExpiryDate < :today AND r.status = :status")
    public List<Reservation> findExpired(ReservationStatus status, LocalDate today);
}