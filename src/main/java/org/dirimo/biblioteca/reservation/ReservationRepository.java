package org.dirimo.biblioteca.reservation;

import org.dirimo.biblioteca.zone.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Zone, Long>{

}
