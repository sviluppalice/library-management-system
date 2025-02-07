package org.dirimo.biblioteca.stock;

import org.dirimo.biblioteca.zone.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Zone, Long>{

}
