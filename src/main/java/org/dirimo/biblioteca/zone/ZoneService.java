package org.dirimo.biblioteca.zone;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepository;

    // Get all zones
    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }

    // Get a zone by ID
    public Optional<Zone> getZoneById(Long id) {
        return zoneRepository.findById(id);
    }

    // Add a new zone
    public Zone saveZone(Zone zone) {
        return zoneRepository.save(zone);
    }

    // Update a zone
    public Zone updateZone(Long id, Zone zone) {
        zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zona con id: " + id + " non trovata."));
        zone.setId(id);
        return zoneRepository.save(zone);
    }

    //Delete a zone
    public void deleteZone(Long id) {
        zoneRepository.deleteById(id);
    }

}