package org.dirimo.biblioteca.resources.shelf;

import lombok.RequiredArgsConstructor;
import org.dirimo.biblioteca.resources.zone.Zone;
import org.dirimo.biblioteca.resources.zone.ZoneRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShelfService {

    private final ShelfRepository shelfRepository;
    private final ZoneRepository zoneRepository;

    @Value("${library.maxReservationPerUser}")
    private int maxReservation;

    // Get all shelves
    public List<Shelf> getAll() {
        return shelfRepository.findAll();
    }

    // Get a shelf by ID
    public Optional<Shelf> getShelfById(Long id) {
        return shelfRepository.findById(id);
    }

    // Get shelves by zone ID
    public List<Shelf> getByZoneId(Long zoneId) {
        return shelfRepository.findByZoneZoneId(zoneId);
    }

    // Add a new shelf
    public Shelf create(Shelf shelf) {
        Long zoneId = shelf.getZone().getZoneId();
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zona con id: " +zoneId+ " non trovata."));
        shelf.setZone(zone);
        return shelfRepository.save(shelf);
    }

    // Update a shelf
    public Shelf update(Long id, Shelf shelf) {
        shelfRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Scaffale con id: " + id + " non trovato."));
        return shelfRepository.save(shelf);
    }

    // Delete a shelf by ID
    public void delete(Long id) {
        shelfRepository.deleteById(id);
    }
}