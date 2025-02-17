package org.dirimo.biblioteca.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{
    @Query("SELECT r FROM Reservation r WHERE r.resExpiryDate BETWEEN :startDate AND :endDate")
    public List<Reservation> findExpiringReservations(LocalDate startDate, LocalDate endDate);
}
