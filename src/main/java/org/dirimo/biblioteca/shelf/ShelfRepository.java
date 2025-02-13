package org.dirimo.biblioteca.shelf;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelfRepository extends JpaRepository<Shelf, Long>{
    List<Shelf> findShelvesByZoneId(Long zoneId);
}
