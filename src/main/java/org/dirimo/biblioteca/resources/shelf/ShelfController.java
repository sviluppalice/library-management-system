package org.dirimo.biblioteca.resources.shelf;

import jakarta.transaction.Transactional;
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
@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("Shelf")
public class ShelfController {

    private final ShelfService shelfService;

    // Get all shelves
    @GetMapping("/")
    public List<Shelf> getAll() {
        return shelfService.getAll();
    }

    // Get shelf by ID
    @GetMapping("/{id}")
    public Shelf getBookById(@PathVariable Long id) {
        return shelfService.getShelfById(id)
                .orElseThrow(() -> new RuntimeException("Scaffale con id " + id + " non trovato."));
    }

    // Get shelves by zone ID
    @GetMapping("/zone")
    public List<Shelf> getByZoneId(@RequestParam Long zoneId) {
        return shelfService.getByZoneId(zoneId);
    }

    // Add a new shelf
    @PostMapping("/")
    public Shelf create(@RequestBody Shelf shelf) {
        return shelfService.create(shelf);
    }

    // Update a shelf
    @PutMapping("/{id}")
    public Shelf update(@PathVariable Long id, @RequestBody Shelf shelf) {
        return shelfService.update(id, shelf);
    }

    // Delete a shelf
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        shelfService.delete(id);
    }
}