package org.dirimo.biblioteca.shelf;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShelfService {

    private final ShelfRepository shelfRepository;

    // Get all shelves
    public List<Shelf> getAllShelves() {
        return shelfRepository.findAll();
    }

    // Get a shelf by ID
    public Optional<Shelf> getShelfById(Long id) {
        return shelfRepository.findById(id);
    }

    // Add a new shelf
    public Shelf saveShelf(Shelf shelf) {
        return shelfRepository.save(shelf);
    }

    // Update a shelf
    public Shelf updateShelf(Long id, Shelf shelf) {
        shelfRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Scaffale con id: " + id + " non trovato."));
        return shelfRepository.save(shelf);
    }

    // Delete a shelf by ID
    public void deleteShelf(Long id) {
        shelfRepository.deleteById(id);
    }
}