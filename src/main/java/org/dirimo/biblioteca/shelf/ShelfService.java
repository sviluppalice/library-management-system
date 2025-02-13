package org.dirimo.biblioteca.shelf;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShelfService {

    private final ShelfRepository shelfRepository;

    @Value("${library.maxReservationPerUser}")
    private int maxReservation;

    // Get all shelves
    public List<Shelf> getAllShelves() {
        return shelfRepository.findAll();
    }

    // Get a shelf by ID
    public Optional<Shelf> getShelfById(Long id) {
        return shelfRepository.findById(id);
    }

    // Get shelves by zone ID
    public List<Shelf> getShelvesByZoneId(Long zoneId) {
        return shelfRepository.findShelvesByZoneId(zoneId);
    }

    // Add a new shelf
    @Transactional
    public Shelf saveShelf(Shelf shelf) {
        return shelfRepository.save(shelf);
    }

    // Update a shelf
    @Transactional
    public Shelf updateShelf(Long id, Shelf shelf) {
        shelfRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Scaffale con id: " + id + " non trovato."));
        shelf.setId(id);
        return shelfRepository.save(shelf);
    }

    // Delete a shelf by ID
    @Transactional
    public void deleteShelf(Long id) {
        shelfRepository.deleteById(id);
    }
}