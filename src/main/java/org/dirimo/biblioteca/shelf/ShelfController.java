package org.dirimo.biblioteca.shelf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Shelf> getBookById(@PathVariable Long id) {
        Optional<Shelf> shelf = shelfService.getShelfById(id);
        if (shelf.isPresent()) {
            return ResponseEntity.ok(shelf.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Add a new shelf
    @PostMapping("/")
    public ResponseEntity<Shelf> createShelf(@RequestBody Shelf shelf) {
        Shelf savedShelf = shelfService.saveShelf(shelf);
        return ResponseEntity.ok(savedShelf);
    }

    // Update a shelf
    @PutMapping("/{id}")
    public ResponseEntity<Shelf> updateShelf(@PathVariable Long id, @RequestBody Shelf shelfDetails) {
        try {
            Shelf updatedShelf = shelfService.updateShelf(id, shelfDetails);
            return ResponseEntity.ok(updatedShelf);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a shelf
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShelf(@PathVariable Long id) {
        shelfService.deleteShelf(id);
        return ResponseEntity.noContent().build();
    }
}