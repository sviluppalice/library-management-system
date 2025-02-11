package org.dirimo.biblioteca.zone;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/zones")
public class ZoneController {

    private final ZoneService zoneService;

    // Get all zones
    @GetMapping("/")
    public List<Zone> getAllZones() {
        return zoneService.getAllZones();
    }

    // Get zone by id
    @GetMapping("/{id}")
    public Zone getZoneById(@PathVariable Long id) {
        return zoneService.getZoneById(id)
                .orElseThrow(() -> new RuntimeException("Zona con id " + id + " non trovata."));
    }

    // Create a zone
    @PostMapping("/")
    public Zone createZone(@RequestBody Zone zone) {
        return zoneService.saveZone(zone);
    }

    // Update a zone
    @PutMapping("/{id}")
    public Zone updateZone(@PathVariable Long id, @RequestBody Zone zone) {
            return zoneService.updateZone(id, zone);
    }

    // Delete a zone
    @DeleteMapping("/{id}")
    public void deleteZone(@PathVariable Long id) {
        zoneService.deleteZone(id);
    }
}
