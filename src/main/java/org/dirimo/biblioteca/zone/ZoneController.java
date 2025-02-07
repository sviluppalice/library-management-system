package org.dirimo.biblioteca.zone;

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
@RequestMapping("api/zone")
public class ZoneController {

    private final ZoneService zoneService;

    // Get all zones
    @GetMapping("/")
    public List<Zone> getAllZones() {
        return zoneService.getAllZones();
    }

    // Get zone by id
    @GetMapping("/{id}")
    public ResponseEntity<Zone> getZoneById(@PathVariable Long id) {
        Optional<Zone> zone = zoneService.getZoneById(id);
        if (zone.isPresent()) {
            return ResponseEntity.ok(zone.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Create a zone
    @PostMapping("/")
    public ResponseEntity<Zone> createZone(@RequestBody Zone zone) {
        Zone savedZone = zoneService.saveZone(zone);
        return ResponseEntity.ok(savedZone);
    }

    // Update a zone
    @PutMapping("/{id}")
    public ResponseEntity<Zone> updateZone(@PathVariable Long id, @RequestBody Zone zone) {
        try {
            Zone updatedZone = zoneService.updateZone(id, zone);
            return ResponseEntity.ok(updatedZone);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a zone
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
        zoneService.deleteZone(id);
        return ResponseEntity.ok().build();
    }
}
