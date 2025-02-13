package org.dirimo.biblioteca.shelf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/shelves")
public class ShelfController {

    private final ShelfService shelfService;

    // Get all shelves
    @GetMapping("/")
    public List<Shelf> getAllShelves() {
        return shelfService.getAllShelves();
    }

    // Get shelf by ID
    @GetMapping("/{id}")
    public Shelf getBookById(@PathVariable Long id) {
        return shelfService.getShelfById(id)
                .orElseThrow(() -> new RuntimeException("Scaffale con id " + id + " non trovato."));
    }

    // Get shelves by zone ID
    @GetMapping("/zone")
    public List<Shelf> getShelvesByZoneId(@RequestParam Long zoneId) {
        return shelfService.getShelvesByZoneId(zoneId);
    }

    // Add a new shelf
    @PostMapping("/")
    public Shelf createShelf(@RequestBody Shelf shelf) {
        return shelfService.saveShelf(shelf);
    }

    // Update a shelf
    @PutMapping("/{id}")
    public Shelf updateShelf(@PathVariable Long id, @RequestBody Shelf shelf) {
        return shelfService.updateShelf(id, shelf);
    }

    // Delete a shelf
    @DeleteMapping("/{id}")
    public void deleteShelf(@PathVariable Long id) {
        shelfService.deleteShelf(id);
    }
}